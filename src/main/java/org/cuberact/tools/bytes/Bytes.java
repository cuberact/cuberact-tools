package org.cuberact.tools.bytes;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Function;

public interface Bytes {

    Bytes EMPTY = new ByteData(0);

    byte get(int index);

    int size();

    byte[] toArray();

    byte[] toArray(final ByteToken token);

    void toStream(OutputStream stream);

    boolean endWith(final byte[] bytes);

    boolean containsOnly(final byte[] bytes);

    String toString(Charset charset);

    String toString(ByteToken token, Charset charset);

    List<ByteToken> scan(Function<Byte, Boolean> tokenDetector);
}
