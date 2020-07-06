package org.cuberact.tools;

import org.cuberact.tools.bytes.ByteConst;
import org.cuberact.tools.bytes.ByteData;
import org.cuberact.tools.bytes.ByteToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ByteDataTest {

    private byte[] data = "1234".getBytes();
    private ByteData bytes;

    @BeforeEach
    public void initBytes() {
        bytes = new ByteData(3);
    }

    @Test
    public void empty() {
        Assertions.assertEquals(0, bytes.size());
        Assertions.assertEquals(0, bytes.toArray().length);
        Assertions.assertEquals("", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void add() {
        bytes.add(data[0]);
        bytes.add(data);
        bytes.add(data, 2);
        Assertions.assertEquals(7, bytes.size());
        Assertions.assertEquals(7, bytes.toArray().length);
        Assertions.assertEquals("1123412", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void drop() {
        bytes.add(data);
        bytes.drop(2);
        Assertions.assertEquals(2, bytes.size());
        Assertions.assertEquals(2, bytes.toArray().length);
        Assertions.assertEquals("12", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void endWith() {
        bytes.add(data);
        Assertions.assertTrue(bytes.endWith("1234".getBytes()));
        Assertions.assertTrue(bytes.endWith("234".getBytes()));
        Assertions.assertTrue(bytes.endWith("34".getBytes()));
        Assertions.assertTrue(bytes.endWith("4".getBytes()));

        Assertions.assertFalse(bytes.endWith("3".getBytes()));
        Assertions.assertFalse(bytes.endWith("23".getBytes()));
        Assertions.assertFalse(bytes.endWith("12345".getBytes()));
    }

    @Test
    public void writeBytesToBytes_sameInstances() {
        bytes.add(data);
        bytes.add(bytes);
        Assertions.assertEquals(8, bytes.size());
        Assertions.assertEquals(8, bytes.toArray().length);
        Assertions.assertEquals("12341234", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void writeBytesToBytes_diffInstances() {
        bytes.add(data);
        bytes.add(new ByteData().add(data));
        Assertions.assertEquals(8, bytes.size());
        Assertions.assertEquals(8, bytes.toArray().length);
        Assertions.assertEquals("12341234", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void tokens1() {
        String line = "GET /something            HTTP/1.1\r\n";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assertions.assertEquals(3, tokens.size());
        Assertions.assertEquals("GET", byteData.toString(tokens.get(0), StandardCharsets.UTF_8));
        Assertions.assertEquals("/something", byteData.toString(tokens.get(1), StandardCharsets.UTF_8));
        Assertions.assertEquals("HTTP/1.1", byteData.toString(tokens.get(2), StandardCharsets.UTF_8));
    }

    @Test
    public void tokens2() {
        String line = "G";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assertions.assertEquals(1, tokens.size());
        Assertions.assertEquals("G", byteData.toString(tokens.get(0), StandardCharsets.UTF_8));
    }

    @Test
    public void tokens3() {
        String line = "";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assertions.assertEquals(0, tokens.size());
    }

    @Test
    public void tokens4() {
        String line = "         \r\n   ";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assertions.assertEquals(0, tokens.size());
    }

}
