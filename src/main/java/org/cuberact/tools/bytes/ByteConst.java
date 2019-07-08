package org.cuberact.tools.bytes;

import java.util.function.Function;

public class ByteConst {

    public static final byte[] CRLF = new byte[]{13, 10};
    public static final byte SPACE = 32; // ' '
    public static final byte COLON = 58; // ':'

    public static final Function<Byte, Boolean> DETECTOR_NOT_WHITE_CHAR = b -> b > 32 && b < 127; //is US-ASCII printable char
    public static final Function<Byte, Boolean> DETECTOR_NOT_CRLF = b -> b != 10 && b != 13; //not CR or LF
    public static final Function<Byte, Boolean> DETECTOR_HEADER_NAME_VALUE = b -> b != 58; // not ':'
}
