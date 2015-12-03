package io.inkstand.scribble.rules;

import static org.junit.Assert.assertNotNull;

import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule may be used to record output written onto the System.out or System.err print streams. The output will
 * still be written to those stream, but the content written is available as String.
 *
 * Created by Gerald Muecke on 19.11.2015.
 */
public class SystemConsole implements TestRule {

    private RecordingPrintStream err;
    private RecordingPrintStream out;

    @Override
    public Statement apply(final Statement statement, final Description description) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                final PrintStream originalOut = System.out;
                final PrintStream originalErr = System.err;
                try {
                    out = new RecordingPrintStream(originalOut);
                    err = new RecordingPrintStream(originalErr);
                    System.setOut(out);
                    System.setErr(err);
                    statement.evaluate();
                } finally {
                    System.setOut(originalOut);
                    System.setErr(originalErr);
                }
            }
        };
    }

    public String getOut(){
        assertNotNull("Rule is not initialized", out);
        return out.getString();
    }

    public String getErr(){
        assertNotNull("Rule is not initialized", err);
        return err.getString();
    }

    /**
     * Proxy that records all written bytes into a StringBuffer that can
     */
    private class RecordingPrintStream extends PrintStream {

        private final StringBuffer buf = new StringBuffer(64);

        public RecordingPrintStream(final OutputStream out) {
            super(out);
        }

        @Override
        public void write(final int b) {
            buf.append((char)b);
            super.write(b);
        }

        @Override
        public void write(final byte[] buf, final int off, final int len) {
            for(int i = off; i < off + len; i++) {
                write(buf[i]);
            }
            super.write(buf, off, len);
        }

        String getString(){
            return buf.toString();
        }
    }
}
