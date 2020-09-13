package org.cuberact.tools.bytes;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.cuberact.tools.stream.ByteInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ByteDataTest {

    @Test
    public void constructor() {
        ByteData byteData = new ByteData();
        Assertions.assertEquals(0, byteData.size());
    }

    @Test
    public void constructorWithExplicitChunkSize() {
        ByteData byteData = new ByteData(10);
        Assertions.assertEquals(0, byteData.size());
    }

    @Test
    public void addByte() {
        ByteData byteData = new ByteData();
        byteData.add((byte) 1);
        byteData.add((byte) 2);
        byteData.add((byte) 3);
        Assertions.assertEquals(3, byteData.size());
        Assertions.assertEquals(1, byteData.get(0));
        Assertions.assertEquals(2, byteData.get(1));
        Assertions.assertEquals(3, byteData.get(2));
    }

    @Test
    public void addArray() {
        byte[] testData = new byte[]{1, 2, 3};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        byteData.add(testData);
        Assertions.assertEquals(6, byteData.size());
    }

    @Test
    public void addArrayWithLength() {
        byte[] testData = new byte[]{1, 2, 3};
        ByteData byteData = new ByteData();
        byteData.add(testData, 2);
        byteData.add(testData, 1);
        Assertions.assertEquals(3, byteData.size());
        Assertions.assertEquals(1, byteData.get(0));
        Assertions.assertEquals(2, byteData.get(1));
        Assertions.assertEquals(1, byteData.get(2));
    }

    @Test
    public void addCharSequence() {
        String testData = "ěščřžýáíé"; //char size 9, utf-8 has two bytes per char
        ByteData byteData = new ByteData();
        byteData.add(testData, StandardCharsets.UTF_8);
        Assertions.assertEquals(18, byteData.size());
        Assertions.assertEquals(testData, byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void addBytes() {
        ByteData testData = new ByteData(new byte[]{1, 2, 3});
        ByteData byteData = new ByteData();
        byteData.add(testData);
        Assertions.assertEquals(3, byteData.size());
        Assertions.assertEquals(1, byteData.get(0));
        Assertions.assertEquals(2, byteData.get(1));
        Assertions.assertEquals(3, byteData.get(2));
    }

    @Test
    public void addBytesWithToken() {
        ByteData testData = new ByteData(new byte[]{1, 2, 3});
        ByteData byteData = new ByteData();
        byteData.add(testData, new ByteToken(0, 1));
        Assertions.assertEquals(2, byteData.size());
        Assertions.assertEquals(1, byteData.get(0));
        Assertions.assertEquals(2, byteData.get(1));
    }

    @Test
    public void addInputStreamWithSeparator() {
        ByteInputStream bais = new ByteInputStream(new ByteArrayInputStream("one two three".getBytes()));
        ByteData byteData = new ByteData();
        byteData.add(bais, " ".getBytes());
        Assertions.assertEquals(4, byteData.size());
        Assertions.assertEquals("one ", byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void addInputStreamLine() {
        ByteData source = new ByteData();
        source.add("line 2", StandardCharsets.UTF_8).add(ByteConst.CR);
        source.add("line 3", StandardCharsets.UTF_8).add(ByteConst.LF);

        ByteInputStream bis = new ByteInputStream(new ByteArrayInputStream(source.toArray()));

        assertEquals(new ByteData().add("line 2", StandardCharsets.UTF_8).add(ByteConst.CR), new ByteData().addLine(bis));
        assertEquals(new ByteData().add("line 3", StandardCharsets.UTF_8).add(ByteConst.LF), new ByteData().addLine(bis));
    }

    private void assertEquals(Bytes expected, Bytes actual) {
        String e = Arrays.toString(expected.toArray());
        String a = Arrays.toString(actual.toArray());
        Assertions.assertEquals(e, a);
    }

    @Test
    public void addInputStreamWithLength() {
        ByteInputStream bais = new ByteInputStream(new ByteArrayInputStream("one two three".getBytes()));
        ByteData byteData = new ByteData();
        byteData.add(bais, 4);
        Assertions.assertEquals(4, byteData.size());
        Assertions.assertEquals("one ", byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void addInputStream() {
        ByteInputStream bais = new ByteInputStream(new ByteArrayInputStream("one two three".getBytes()));
        ByteData byteData = new ByteData();
        byteData.add(bais);
        Assertions.assertEquals(13, byteData.size());
        Assertions.assertEquals("one two three", byteData.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void drop() {
        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        byteData.drop(2);
        Assertions.assertEquals(3, byteData.size());
        Assertions.assertEquals(1, byteData.get(0));
        Assertions.assertEquals(2, byteData.get(1));
        Assertions.assertEquals(3, byteData.get(2));
    }

    @Test
    public void clear() {
        byte[] testData = new byte[]{1, 2, 3};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        Assertions.assertEquals(3, byteData.size());
        Assertions.assertEquals(1, byteData.get(0));
        Assertions.assertEquals(2, byteData.get(1));
        Assertions.assertEquals(3, byteData.get(2));
        byteData.clear();
        Assertions.assertEquals(0, byteData.size());
    }

    @Test
    public void toArrayWithToken() {
        byte[] testData = new byte[]{1, 2, 3, 4, 5};
        ByteData byteData = new ByteData();
        byteData.add(testData);
        final byte[] array = byteData.toArray(new ByteToken(2, 4));
        Assertions.assertEquals(3, array.length);
        Assertions.assertEquals(3, array[0]);
        Assertions.assertEquals(4, array[1]);
        Assertions.assertEquals(5, array[2]);
    }

    @Test
    public void scan() {
        ByteData byteData = new ByteData();
        byteData.add("GET / HTTP/1.1\r\n", StandardCharsets.UTF_8);
        final List<ByteToken> tokens = byteData.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assertions.assertEquals(3, tokens.size());
        Assertions.assertEquals("GET", byteData.toString(tokens.get(0), StandardCharsets.UTF_8));
        Assertions.assertEquals("/", byteData.toString(tokens.get(1), StandardCharsets.UTF_8));
        Assertions.assertEquals("HTTP/1.1", byteData.toString(tokens.get(2), StandardCharsets.UTF_8));

    }

}