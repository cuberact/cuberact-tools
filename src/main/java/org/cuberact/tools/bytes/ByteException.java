package org.cuberact.tools.bytes;

public final class ByteException extends RuntimeException {
    ByteException(final String message) {
        super(message);
    }

    ByteException(final Throwable t) {
        super(t);
    }
}
