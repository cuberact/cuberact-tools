package org.cuberact.tools;

import org.cuberact.tools.bytes.ByteConst;
import org.cuberact.tools.bytes.ByteData;
import org.cuberact.tools.bytes.ByteToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ByteDataTest {

    private byte[] data = "1234".getBytes();
    private ByteData bytes;

    @Before
    public void initBytes() {
        bytes = new ByteData(3);
    }

    @Test
    public void empty() {
        Assert.assertEquals(0, bytes.size());
        Assert.assertEquals(0, bytes.toArray().length);
        Assert.assertEquals("", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void add() {
        bytes.add(data[0]);
        bytes.add(data);
        bytes.add(data, 2);
        Assert.assertEquals(7, bytes.size());
        Assert.assertEquals(7, bytes.toArray().length);
        Assert.assertEquals("1123412", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void drop() {
        bytes.add(data);
        bytes.drop(2);
        Assert.assertEquals(2, bytes.size());
        Assert.assertEquals(2, bytes.toArray().length);
        Assert.assertEquals("12", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void endWith() {
        bytes.add(data);
        Assert.assertTrue(bytes.endWith("1234".getBytes()));
        Assert.assertTrue(bytes.endWith("234".getBytes()));
        Assert.assertTrue(bytes.endWith("34".getBytes()));
        Assert.assertTrue(bytes.endWith("4".getBytes()));

        Assert.assertFalse(bytes.endWith("3".getBytes()));
        Assert.assertFalse(bytes.endWith("23".getBytes()));
        Assert.assertFalse(bytes.endWith("12345".getBytes()));
    }

    @Test
    public void writeBytesToBytes_sameInstances() {
        bytes.add(data);
        bytes.add(bytes);
        Assert.assertEquals(8, bytes.size());
        Assert.assertEquals(8, bytes.toArray().length);
        Assert.assertEquals("12341234", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void writeBytesToBytes_diffInstances() {
        bytes.add(data);
        bytes.add(new ByteData().add(data));
        Assert.assertEquals(8, bytes.size());
        Assert.assertEquals(8, bytes.toArray().length);
        Assert.assertEquals("12341234", bytes.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void tokens1() {
        String line = "GET /something            HTTP/1.1\r\n";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals("GET", byteData.toString(tokens.get(0), StandardCharsets.UTF_8));
        Assert.assertEquals("/something", byteData.toString(tokens.get(1), StandardCharsets.UTF_8));
        Assert.assertEquals("HTTP/1.1", byteData.toString(tokens.get(2), StandardCharsets.UTF_8));
    }

    @Test
    public void tokens2() {
        String line = "G";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assert.assertEquals(1, tokens.size());
        Assert.assertEquals("G", byteData.toString(tokens.get(0), StandardCharsets.UTF_8));
    }

    @Test
    public void tokens3() {
        String line = "";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assert.assertEquals(0, tokens.size());
    }

    @Test
    public void tokens4() {
        String line = "         \r\n   ";
        ByteData byteData = new ByteData().add(line.getBytes());
        List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assert.assertEquals(0, tokens.size());
    }

}
