package org.cuberact.tools.bytes;

import java.io.IOException;
import java.io.InputStream;

public class ByteInputStream extends InputStream {

    private final InputStream stream;
    private boolean repeatLastByte = false;
    private int lastByte;

    public ByteInputStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public int read() throws IOException {
        if (repeatLastByte) {
            repeatLastByte = false;
            return lastByte;
        }
        lastByte = stream.read();
        return lastByte;
    }

    public int available() throws IOException {
        int available = stream.available();
        if (repeatLastByte) available++;
        return available;
    }

    public void repeatLastByte() {
        repeatLastByte = true;
    }
}
