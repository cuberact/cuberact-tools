package org.cuberact.tools.bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ABytes implements Bytes {

    @Override
    public abstract byte get(int index);

    @Override
    public abstract int size();

    @Override
    public abstract byte[] toArray();

    public boolean endWith(final byte[] bytes) {
        final int dataIndex = size() - bytes.length;
        if (dataIndex < 0) return false;
        for (int bytesIndex = 0; bytesIndex < bytes.length; bytesIndex++) {
            if (bytes[bytesIndex] != get(bytesIndex + dataIndex)) return false;
        }
        return true;
    }

    public boolean containsOnly(final byte[] bytes) {
        if (size() != bytes.length) return false;
        for (int i = 0; i < bytes.length; i++) {
            if (get(i) != bytes[i]) return false;
        }
        return true;
    }

    public byte[] toArray(final ByteToken token) {
        int f = token.from() < 0 ? 0 : token.from();
        final int t = token.to() > size() ? size() : token.to();
        if (f == 0 && t == size()) return toArray();
        final int length = t - f;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = get(f++);
        }
        return result;
    }

    @Override
    public void writeTo(final OutputStream stream) {
        final int size = size();
        try {
            for (int i = 0; i < size; i++) {
                stream.write(get(i));
            }
        } catch (IOException e) {
            throw new ByteException(e);
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + System.identityHashCode(this) + "] - size = " + size();
    }

    public String toString(Charset charset) {
        return new String(toArray(), charset);
    }

    public String toString(ByteToken token, Charset charset) {
        return new String(toArray(token), charset);
    }

    public List<ByteToken> scan(Function<Byte, Boolean> tokenDetector) {
        List<ByteToken> tokens = new ArrayList<>();
        final int size = size();
        int start = 0;
        for (int i = 0; i < size; i++) {
            if (tokenDetector.apply(get(i)) == Boolean.FALSE) {
                if (i > start) {
                    tokens.add(new ByteToken(start, i));
                }
                start = i + 1;
            }
        }
        if (start < size) {
            tokens.add(new ByteToken(start, size));
        }
        return tokens;
    }

}
