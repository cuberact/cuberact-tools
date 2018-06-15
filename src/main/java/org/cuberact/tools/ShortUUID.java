/*
 * Copyright 2017 Michal Nikodim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cuberact.tools;

import java.util.UUID;

/**
 * <p>Shorter UUID represantation. Have same strength as java.util.UUID. It's useful for save to database as ID or everywhere where we need persist unique identifier and we need shorter then java UUID.</p>
 * <p>ShortUUID always has 26 chars. For compare java UUID has 36 chars.</p>
 * <p>ShortUUID uses only chars: abcdefghijklmnopqrstuvwxyz012345</p>
 * <p>Implementation of ShortUUID is heavy optimized for speed.</p>
 *
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class ShortUUID {

    private static final char[] charMap = "abcdefghijklmnopqrstuvwxyz012345".toCharArray();
    private static final long[] decode = new long[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};

    private ShortUUID() {
        //utility class
    }

    /**
     * @return new short UUID
     */
    public static String randomUUID() {
        return encode(UUID.randomUUID());
    }

    /**
     * @param uuid - java UUID
     * @return create short UUID from existing one
     */
    public static String encode(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        char[] buf = new char[26];
        int i = 25;
        for (int j = 0; j < 12; j++) {
            buf[i--] = charMap[(int) (lsb & 31)];
            lsb >>>= 5;
        }
        buf[i--] = charMap[(int) (lsb | ((msb & 1) << 4))];
        msb >>>= 1;
        for (int j = 0; j < 12; j++) {
            buf[i--] = charMap[(int) (msb & 31)];
            msb >>>= 5;
        }
        buf[i] = charMap[(int) msb];
        return new String(buf);
    }

    /**
     * @param shortUUID - short UUID representation
     * @return convert shortUUID back to java UUID representation
     */
    public static UUID decode(String shortUUID) {
        byte[] data = shortUUID.getBytes();
        long msb = decode[data[0]] << 61;
        msb |= decode[data[1]] << 56;
        msb |= decode[data[2]] << 51;
        msb |= decode[data[3]] << 46;
        msb |= decode[data[4]] << 41;
        msb |= decode[data[5]] << 36;
        msb |= decode[data[6]] << 31;
        msb |= decode[data[7]] << 26;
        msb |= decode[data[8]] << 21;
        msb |= decode[data[9]] << 16;
        msb |= decode[data[10]] << 11;
        msb |= decode[data[11]] << 6;
        msb |= decode[data[12]] << 1;
        msb |= decode[data[13]] >>> 4;
        long lsb = decode[data[13]] << 60;
        lsb |= decode[data[14]] << 55;
        lsb |= decode[data[15]] << 50;
        lsb |= decode[data[16]] << 45;
        lsb |= decode[data[17]] << 40;
        lsb |= decode[data[18]] << 35;
        lsb |= decode[data[19]] << 30;
        lsb |= decode[data[20]] << 25;
        lsb |= decode[data[21]] << 20;
        lsb |= decode[data[22]] << 15;
        lsb |= decode[data[23]] << 10;
        lsb |= decode[data[24]] << 5;
        lsb |= decode[data[25]];
        return new UUID(msb, lsb);
    }
}
