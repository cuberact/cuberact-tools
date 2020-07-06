package org.cuberact.tools.bytes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ByteMultiTest {

    private ByteMulti byteMulti;

    @BeforeEach
    public void setup() {
        ByteData bd1 = new ByteData();
        ByteData bd2 = new ByteData();
        ByteData bd3 = new ByteData();

        bd1.add("one ", StandardCharsets.UTF_8);
        bd2.add(" two ", StandardCharsets.UTF_8);
        bd3.add("three ", StandardCharsets.UTF_8);

        byteMulti = new ByteMulti(bd1, bd2, bd3);
    }

    @Test
    public void toArray() {
        final byte[] array = byteMulti.toArray();
        Assertions.assertEquals("one  two three ", new String(array));
    }

    @Test
    public void toArrayWithToken() {
        final byte[] array = byteMulti.toArray(new ByteToken(1, 10));
        Assertions.assertEquals(10, array.length);
        Assertions.assertEquals("ne  two th", new String(array));
    }

    @Test
    public void scan() {
        final List<ByteToken> tokens = byteMulti.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assertions.assertEquals(3, tokens.size());
        Assertions.assertEquals("one", byteMulti.toString(tokens.get(0), StandardCharsets.UTF_8));
        Assertions.assertEquals("two", byteMulti.toString(tokens.get(1), StandardCharsets.UTF_8));
        Assertions.assertEquals("three", byteMulti.toString(tokens.get(2), StandardCharsets.UTF_8));
        Assertions.assertEquals(0, tokens.get(0).from());
        Assertions.assertEquals(2, tokens.get(0).to());
        Assertions.assertEquals(3, tokens.get(0).size());
        Assertions.assertEquals(5, tokens.get(1).from());
        Assertions.assertEquals(7, tokens.get(1).to());
        Assertions.assertEquals(3, tokens.get(1).size());
        Assertions.assertEquals(9, tokens.get(2).from());
        Assertions.assertEquals(13, tokens.get(2).to());
        Assertions.assertEquals(5, tokens.get(2).size());
    }

    @Test
    public void containsOnly() {
        Assertions.assertTrue(byteMulti.containsOnly("one  two three ".getBytes()));
        Assertions.assertFalse(byteMulti.containsOnly("one".getBytes()));
    }

    @Test
    public void writeTo() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteMulti.writeTo(baos);
        Assertions.assertEquals("one  two three ", new String(baos.toByteArray()));
    }

    @Test
    public void writeToWithLen() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteMulti.writeTo(baos,3);
        Assertions.assertEquals("one", new String(baos.toByteArray()));
    }

    @Test
    public void writeToWithLenAndWrongLen() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteMulti.writeTo(baos,999);
        Assertions.assertEquals("one  two three ", new String(baos.toByteArray()));
    }

    @Test
    public void writeToWithLenAndZeroLen() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteMulti.writeTo(baos,0);
        Assertions.assertEquals("", new String(baos.toByteArray()));
    }

    @Test
    public void endWith() {
        Assertions.assertTrue(byteMulti.endWith(" ".getBytes()));
        Assertions.assertTrue(byteMulti.endWith("three ".getBytes()));
        Assertions.assertFalse(byteMulti.endWith("three".getBytes()));
    }

}
