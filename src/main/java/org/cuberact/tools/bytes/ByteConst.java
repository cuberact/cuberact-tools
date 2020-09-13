package org.cuberact.tools.bytes;

import java.util.function.Function;

public final class ByteConst {

    public static final byte CR = 13;
    public static final byte LF = 10;
    //public static final byte[] CRLF = new byte[]{CR, LF};  //TODO weird bug in Kotlin
    public static final byte SPACE = 32; // ' '
    public static final byte COLON = 58; // ':'

    public static final Function<Byte, Boolean> TOKEN_DETECTOR_NOT_WHITE_CHAR = b -> b > 32 && b < 127; //is US-ASCII printable char
    public static final Function<Byte, Boolean> TOKEN_DETECTOR_NOT_CRLF = b -> b != 10 && b != 13; //not CR or LF
    public static final Function<Byte, Boolean> TOKEN_DETECTOR_NOT_COLON = b -> b != 58; // not ':'

    private ByteConst() {
        //const class
    }
}
