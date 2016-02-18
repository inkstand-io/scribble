/*
 * Copyright 2015-2016 DevCon5 GmbH, info@devcon5.ch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.inkstand.scribble.net;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import io.inkstand.scribble.rules.ExternalResource;
import org.slf4j.Logger;

/**
 * A rule that starts a server thread accepting incoming UDP packages.
 */
public class UDPReceiver extends ExternalResource {

    private static final Logger LOG = getLogger(UDPReceiver.class);

    private int serverPort = -1;
    private int bufferSize = 2048;
    private PacketHandler packetHandler = new PacketHandler() {
        @Override
        public void process(final byte[] data) {
            packets.addLast(data);
        }
    };

    private ExecutorService threadPool;
    private final Deque<byte[]> packets = new ConcurrentLinkedDeque<>();
    private UDPProcessor processor;

    @Override
    protected void before() throws Throwable {

        if(this.serverPort <= 0){
            this.serverPort = NetworkUtils.findAvailablePort();
        }
        this.threadPool = Executors.newFixedThreadPool(1);
        this.processor = new UDPProcessor(this.serverPort, this.bufferSize, this.packetHandler);
        this.threadPool.submit(this.processor);
        //waiting for the server to come up
        while(!this.processor.running.get()){
            Thread.sleep(10);
        }

    }

    @Override
    protected void after() {
        this.processor.stop();
        this.threadPool.shutdownNow();
        try {
            threadPool.awaitTermination(5, SECONDS);
        } catch (InterruptedException e) {
            //omit
        }
    }

    @Override
    protected void beforeClass() throws Throwable {

        before();
    }

    @Override
    protected void afterClass() {

        after();
    }

    /**
     * Checks if there are more packets in the receive queue.
     * @return
     *  true if there are packets available in the receive queue
     */
    public boolean hasMorePackets(){
        return !this.packets.isEmpty();
    }

    /**
     * Returns the next packet from the receive queue
     * @return
     *  the binary data representing the packet
     */
    public byte[] nextPacket(){
        return this.packets.removeFirst();
    }

    /**
     * The number of packets in the queue.
     * @return
     *  the number of packets in the queue.
     */
    public int packetCount(){
        return this.packets.size();
    }

    /**
     * Sets the size of the receive buffer. If the buffer is too small for the received bytes, the remainder of the
     * received packet is silently discarded. This method has to be invoked before the rule is applied, otherwise it
     * won't have  an effect.
     * Default is 2048 bytes.
     * @param bufferSize
     *  the size of the new buffer.
     */
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Sets the server port to a specific port. If no port is selected or this value is less or equal 0, an available
     * random port is used
     * @param serverPort
     */
    public void setServerPort(final int serverPort) {
        assertStateBefore(State.BEFORE_EXECUTED);
        this.serverPort = serverPort;
    }

    /**
     * Returns the port of the sever. In case no particular port has been set, an available random port is chosen
     * on application of the rule.
     * @return
     *  the current tcp port
     */
    public int getServerPort() {

        return serverPort;
    }

    /**
     * Overrides the default packet handler. Note that the methods for reading the received packets won't produce
     * sensible results.
     * @param packetHandler
     *  the new packet handler that is invoked when an UDP datagram is received
     */
    public void onDatagramReceived(final PacketHandler packetHandler) {

        this.packetHandler = packetHandler;
    }

    /**
     * Processor for incoming
     */
    private static class UDPProcessor implements Runnable {

        private static final Logger LOG = getLogger(UDPProcessor.class);

        private AtomicBoolean running = new AtomicBoolean(false);

        /**
         * Listen port of the UDP receiver
         */
        private final int port;
        /**
         * The size of the internal buffer to receive packets
         */
        private final int bufferSize;
        /**
         * The handler that processes the incoming packets
         */
        private final PacketHandler handler;

        /**
         * Stops the server
         */
        public void stop(){
            running.set(false);
        }

        /**
         * Creates a new UDPProcessor on the specified port.
         * @param port
         *  the port to listen for UDP packets
         * @param bufferSize
         *  the buffer to read incoming UDP packets. If a packet is larger that the buffer length, the remainder is
         *  discarded silently.
         * @param handler
         *  the handler that is invoked on received packets
         */
        public UDPProcessor(final int port, final int bufferSize, final PacketHandler handler) {

            this.port = port;
            this.bufferSize = bufferSize;
            this.handler = handler;
        }

        @Override
        public void run() {
            try (DatagramChannel channel = DatagramChannel.open()){
                channel.socket().bind(new InetSocketAddress(this.port));
                final ByteBuffer buf = ByteBuffer.allocate(this.bufferSize);
                //setting running true after the buffer allocation
                //to support waiting for the server to initialize properly
                running.set(true);
                while (running.get()) {
                    receivePacket(channel, buf);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not start UDP receiver");
            }
            LOG.info("Server stopped");
        }

        /**
         * Waits for an incoming packet. If a packet has been received, its read from the buffer and passed
         * to the packet handler
         * @param channel
         *  the datagram channel to receive incoming UDP packets
         * @param buf
         *  the buffer where the packets are received
         */
        private void receivePacket(final DatagramChannel channel, final ByteBuffer buf) {
            buf.clear();
            try {
                channel.receive(buf);
            } catch (IOException e) {
                LOG.warn("Could not read packet");
            }
            buf.flip();
            if(buf.remaining() > 0){
                final byte[] receivedData = new byte[buf.remaining()];
                buf.get(receivedData);
                handler.process(receivedData);
            }
        }

    }
}
