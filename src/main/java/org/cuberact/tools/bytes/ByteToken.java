package org.cuberact.tools.bytes;

public final class ByteToken {

    final int from;
    final int to;

    public ByteToken(final int from, final int to) {
        this.from = from;
        this.to = to;
    }

    public int from() {
        return from;
    }

    public int to() {
        return to;
    }

    public int size() {
        return (to - from) + 1;
    }
}
