package io.inkstand.scribble.net;

import java.net.ServerSocket;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assume.assumeTrue;

/**
 * Created by Gerald M&uuml;cke on 11.03.2015.
 *
 * @author <a href="mailto:gerald.muecke@gmail.com">Gerald M&uuml;cke</a>
 */
public final class NetworkUtils {

    private NetworkUtils() {

    }

    private static final Random RANDOM = new Random();

    /**
     * The port offset may be configured at system level using the system property {@code scribble.net.portOffset}. The
     * offset defines the range of port-numbers used for random port search. The default range is 1024-65535. Any offset
     * will increase the lower bound, but not the upper. For example, setting a port offset of 10000 will result in a
     * range of 11024-65535. The port offset is an atomic integer value that is initialized using the system property
     * (default is 0). It may be set during runtime by setting the value of the port offset directly.
     */
    public static final AtomicInteger PORT_OFFSET = new AtomicInteger(Integer.parseInt(System.getProperty(
            "scribble.net.portOffset",
            "0")));

    /**
     * The default retry count specifies how many times the findAvailablePort method should try to find an
     * available port of no number of retries was specified. The default value can be configured using the
     * system property {@code scribble.net.maxRetries}. The default value is 3. It may be modified at runtime by
     * accessing the AtomicInteger directly.
     */
    public static final AtomicInteger DEFAULT_RETRY_COUNT = new AtomicInteger(Integer.parseInt(System.getProperty(
            "scribble.net.maxRetries",
            "3")));
    ;

    /**
     * Finds an available port. Maximum number of retries is 3 before an {@link org.junit.internal
     * .AssumptionViolatedException} is thrown.
     *
     * @return the number of the port that is available
     */
    public static int findAvailablePort() {

        return findAvailablePort(DEFAULT_RETRY_COUNT.get());
    }

    /**
     * Finds an available port.
     *
     * @param maxRetries
     *         the maximum number of retries before an {@link org.junit.internal .AssumptionViolatedException} is
     *         thrown.
     *
     * @return the number of the port that is available
     */
    public static int findAvailablePort(int maxRetries) {

        int retries = 0;
        int randomPort;
        boolean portAvailable;
        do {
            randomPort = randomPort();
            portAvailable = isPortAvailable(randomPort);
        } while (retries++ < maxRetries && !portAvailable);
        if (retries > maxRetries) {
            assumeTrue("no open port found", portAvailable);
        }
        return randomPort;
    }

    /**
     * Checks if the specified is available as listen port
     *
     * @param port
     *         the port to check
     *
     * @return true if the port is available
     */
    public static boolean isPortAvailable(final int port) {

        try (ServerSocket socket = new ServerSocket(port)) {

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates a random port number above 1024
     *
     * @return
     */
    public static int randomPort() {

        int offset = PORT_OFFSET.get();
        return RANDOM.nextInt(65535 - 1024 - offset) + 1024 + offset;
    }
}
