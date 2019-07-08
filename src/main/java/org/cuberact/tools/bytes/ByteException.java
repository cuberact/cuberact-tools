package org.cuberact.tools.bytes;

public class ByteException extends RuntimeException {
    ByteException(String message) {
        super(message);
    }

    ByteException(Throwable t) {
        super(t);
    }
}
