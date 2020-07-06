package org.cuberact.tools.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Stream {

    private Stream() {
        //utility class
    }

    public static void copy(InputStream is, OutputStream os) {
        try {
            byte[] buffer = new byte[16384];
            int len;
            do {
                len = is.read(buffer);
                if (len > 0) {
                    os.write(buffer, 0, len);
                    if (is.available() < 1) {
                        os.flush();
                    }
                }
            } while (len >= 0);
        } catch (IOException e) {
            throw new StreamException(e);
        }

    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    public static void flushQuietly(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.flush();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    public static void setSoTimeoutQuietly(Socket socket, int timeoutInMs) {
        try {
            socket.setSoTimeout(timeoutInMs);
        } catch (SocketException e) {
            //do nothing
        }
    }

    public static class StreamException extends RuntimeException {

        private StreamException(Throwable cause) {
            super(cause);
        }

    }
}
