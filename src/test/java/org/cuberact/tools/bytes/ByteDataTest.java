package org.cuberact.tools.bytes;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ByteDataTest {

    @Test
    public void constructor() {
        ByteData byteData = new ByteData();
        Assert.assertEquals(0, byteData.size());
    }

    @Test
    public void constructorWithExplicitChunkSize() {
        ByteData byteData = new ByteData(10);
        Assert.assertEquals(0, byteData.size());
    }

    @Test
    public void addByte() {
        ByteData byteData = new ByteData();
        byteData.add((byte) 1);
        byteData.add((byte) 2);
        byteData.add((byte) 3);
        Assert.assertEquals(3, byteData.size());
        Assert.assertEquals(1, byteData.get(0));
        Assert.assertEquals(2, byteData.get(1));
        Assert.assertEquals(3, byteData.get(2));
    }

    @Test
    public void addArray() {
        byte[] testData = new byte[]{1, 2, 3};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        byteData.add(testData);
        Assert.assertEquals(6, byteData.size());
    }

    @Test
    public void addArrayWithLength() {
        byte[] testData = new byte[]{1, 2, 3};
        ByteData byteData = new ByteData();
        byteData.add(testData, 2);
        byteData.add(testData, 1);
        Assert.assertEquals(3, byteData.size());
        Assert.assertEquals(1, byteData.get(0));
        Assert.assertEquals(2, byteData.get(1));
        Assert.assertEquals(1, byteData.get(2));
    }

    @Test
    public void addCharSequence() {
        String testData = "ěščřžýáíé"; //char size 9, utf-8 has two bytes per char
        ByteData byteData = new ByteData();
        byteData.add(testData, StandardCharsets.UTF_8);
        Assert.assertEquals(18, byteData.size());
        Assert.assertEquals(testData, byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void addBytes() {
        ByteData testData = new ByteData(new byte[]{1, 2, 3});
        ByteData byteData = new ByteData();
        byteData.add(testData);
        Assert.assertEquals(3, byteData.size());
        Assert.assertEquals(1, byteData.get(0));
        Assert.assertEquals(2, byteData.get(1));
        Assert.assertEquals(3, byteData.get(2));
    }

    @Test
    public void addBytesWithToken() {
        ByteData testData = new ByteData(new byte[]{1, 2, 3});
        ByteData byteData = new ByteData();
        byteData.add(testData, new ByteToken(0, 1));
        Assert.assertEquals(2, byteData.size());
        Assert.assertEquals(1, byteData.get(0));
        Assert.assertEquals(2, byteData.get(1));
    }

    @Test
    public void addInputStreamWithSeparator() {
        ByteInputStream bais = new ByteInputStream(new ByteArrayInputStream("one two three".getBytes()));
        ByteData byteData = new ByteData();
        byteData.add(bais, " ".getBytes());
        Assert.assertEquals(4, byteData.size());
        Assert.assertEquals("one ", byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void addInputStreamLine() {
        ByteData source = new ByteData();
        source.add("line 1", StandardCharsets.UTF_8).add(ByteConst.CRLF);
        source.add("line 2", StandardCharsets.UTF_8).add(ByteConst.CR);
        source.add("line 3", StandardCharsets.UTF_8).add(ByteConst.LF);
        source.add("line 4", StandardCharsets.UTF_8).add(ByteConst.CRLF);
        source.add(ByteConst.CRLF);
        source.add(ByteConst.CR);
        source.add(ByteConst.CR);
        source.add(ByteConst.CRLF);
        source.add(ByteConst.LF);
        source.add(ByteConst.LF);

        ByteInputStream bis = new ByteInputStream(new ByteArrayInputStream(source.toArray()));

        assertEquals(new ByteData().add("line 1", StandardCharsets.UTF_8).add(ByteConst.CRLF), new ByteData().addLine(bis));
        assertEquals(new ByteData().add("line 2", StandardCharsets.UTF_8).add(ByteConst.CR), new ByteData().addLine(bis));
        assertEquals(new ByteData().add("line 3", StandardCharsets.UTF_8).add(ByteConst.LF), new ByteData().addLine(bis));
        assertEquals(new ByteData().add("line 4", StandardCharsets.UTF_8).add(ByteConst.CRLF), new ByteData().addLine(bis));
        assertEquals(new ByteData().add(ByteConst.CRLF), new ByteData().addLine(bis));
        assertEquals(new ByteData().add(ByteConst.CR), new ByteData().addLine(bis));
        assertEquals(new ByteData().add(ByteConst.CR), new ByteData().addLine(bis));
        assertEquals(new ByteData().add(ByteConst.CRLF), new ByteData().addLine(bis));
        assertEquals(new ByteData().add(ByteConst.LF), new ByteData().addLine(bis));
        assertEquals(new ByteData().add(ByteConst.LF), new ByteData().addLine(bis));
    }

    private void assertEquals(Bytes expected, Bytes actual) {
        String e = Arrays.toString(expected.toArray());
        String a = Arrays.toString(actual.toArray());
        Assert.assertEquals(e, a);
    }

    @Test
    public void addInputStreamWithLength() {
        ByteInputStream bais = new ByteInputStream(new ByteArrayInputStream("one two three".getBytes()));
        ByteData byteData = new ByteData();
        byteData.add(bais, 4);
        Assert.assertEquals(4, byteData.size());
        Assert.assertEquals("one ", byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void addInputStream() {
        ByteInputStream bais = new ByteInputStream(new ByteArrayInputStream("one two three".getBytes()));
        ByteData byteData = new ByteData();
        byteData.add(bais);
        Assert.assertEquals(13, byteData.size());
        Assert.assertEquals("one two three", byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void drop() {
        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        byteData.drop(2);
        Assert.assertEquals(3, byteData.size());
        Assert.assertEquals(1, byteData.get(0));
        Assert.assertEquals(2, byteData.get(1));
        Assert.assertEquals(3, byteData.get(2));
    }

    @Test
    public void clear() {
        byte[] testData = new byte[]{1, 2, 3};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        Assert.assertEquals(3, byteData.size());
        Assert.assertEquals(1, byteData.get(0));
        Assert.assertEquals(2, byteData.get(1));
        Assert.assertEquals(3, byteData.get(2));
        byteData.clear();
        Assert.assertEquals(0, byteData.size());
    }

    @Test
    public void toArrayWithToken() {
        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        final byte[] array = byteData.toArray(new ByteToken(2, 4));
        Assert.assertEquals(3, array.length);
        Assert.assertEquals(3, array[0]);
        Assert.assertEquals(4, array[1]);
        Assert.assertEquals(5, array[2]);
    }

    @Test
    public void scan() {
        ByteData byteData = new ByteData();
        byteData.add("GET / HTTP/1.1\r\n", StandardCharsets.UTF_8);
        final List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals("GET", byteData.toString(tokens.get(0), StandardCharsets.UTF_8));
        Assert.assertEquals("/", byteData.toString(tokens.get(1), StandardCharsets.UTF_8));
        Assert.assertEquals("HTTP/1.1", byteData.toString(tokens.get(2), StandardCharsets.UTF_8));

    }

}