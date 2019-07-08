package org.cuberact.tools.bytes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

public final class ByteData extends ABytes {

    private static final int CHUNK_SIZE = 4096;
    private static final int MAX_DATA_SIZE = Integer.MAX_VALUE - 8;

    private final int chunkSize;
    private byte[] data;
    private int size;

    public ByteData() {
        this(CHUNK_SIZE);
    }

    public ByteData(final int chunkSize) {
        this.data = new byte[chunkSize];
        this.chunkSize = chunkSize;
        this.size = 0;
    }

    public ByteData(final byte[] data) {
        this(data, CHUNK_SIZE);
    }

    public ByteData(final byte[] data, final int chunkSize) {
        this.data = data;
        this.size = data.length;
        this.chunkSize = chunkSize;
    }

    public ByteData add(final byte b) {
        addToData(b);
        return this;
    }

    public ByteData add(final byte[] bytes) {
        return add(bytes, bytes.length);
    }

    public ByteData add(final byte[] bytes, final int length) {
        if (bytes.length > 0) {
            for (int i = 0; i < length; i++) {
                addToData(bytes[i]);
            }
        }
        return this;
    }

    public ByteData add(final CharSequence sequence, final Charset charset) {
        if (sequence == null) return this;
        return add(sequence.toString().getBytes(charset));
    }

    public ByteData add(final Bytes bytes) {
        final int byteSize = bytes.size(); //important, because param byteData can be this (same instance)
        for (int i = 0; i < byteSize; i++) {
            addToData(bytes.get(i));
        }
        return this;
    }

    public ByteData add(final Bytes bytes, final ByteToken token) {
        for (int i = token.from; i <= token.to; i++) {
            addToData(bytes.get(i));
        }
        return this;
    }

    public ByteData add(final InputStream stream, final byte[] separator) {
        while (true) {
            add(readByte(stream));
            if (endWith(separator)) {
                return this;
            }
        }
    }

    public ByteData add(final InputStream stream, final int length) {
        for (int i = 0; i < length; i++) {
            addToData(readByte(stream));
        }
        return this;
    }

    public ByteData add(final InputStream stream) {
        while (true) {
            try {
                addToData(readByte(stream));
                if (stream.available() == 0) {
                    return this;
                }
            } catch (Throwable t) {
                throw new ByteException(t);
            }
        }
    }

    public ByteData drop(final int byteCount) {
        if (byteCount > 0) {
            size -= byteCount;
            size = size < 0 ? 0 : size;
        }
        return this;
    }

    public ByteData clear() {
        size = 0;
        return this;
    }

    public ByteData align() {
        if (size != data.length) {
            data = Arrays.copyOf(data, size);
        }
        return this;
    }

    @Override
    public byte get(final int index) {
        return data[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public byte[] toArray() {
        align();
        return data;
    }

    @Override
    public byte[] toArray(final ByteToken token) {
        final int sizeMinusOne = size() - 1;
        final int f = token.from() < 0 ? 0 : token.from();
        final int t = token.to() > sizeMinusOne ? sizeMinusOne : token.to();
        if (f == 0 && t == sizeMinusOne) return toArray();
        return Arrays.copyOfRange(data, f, t + 1);
    }

    private void addToData(final byte b) {
        ensureDataSize();
        data[size++] = b;
    }

    private void ensureDataSize() {
        if (size == data.length) {
            int newSize = data.length + chunkSize;
            if (newSize > MAX_DATA_SIZE) newSize = MAX_DATA_SIZE;
            if (newSize > data.length) {
                byte[] enlarged = new byte[newSize];
                System.arraycopy(data, 0, enlarged, 0, data.length);
                data = enlarged;
            }
            if (chunkSize == 0) throw new ByteException("Chunk size is zero");
            if (size == data.length) throw new ByteException("Ensure data size fail. Too big [" + size + "]");
        }
    }

    private static byte readByte(final InputStream inputStream) throws ByteException {
        try {
            int intValue = inputStream.read();
            if (intValue == -1) {
                throw new ByteException("Unexpected end of stream");
            }
            return (byte) intValue;
        } catch (IOException e) {
            throw new ByteException(e);
        }
    }
}
