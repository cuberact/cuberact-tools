package org.cuberact.tools.bytes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ByteMultiTest {

    private ByteMulti byteMulti;

    @Before
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
        Assert.assertEquals("one  two three ", new String(array));
    }

    @Test
    public void toArrayWithToken() {
        final byte[] array = byteMulti.toArray(new ByteToken(1, 10));
        Assert.assertEquals(10, array.length);
        Assert.assertEquals("ne  two th", new String(array));
    }

    @Test
    public void scan() {
        final List<ByteToken> tokens = byteMulti.scan(ByteConst.TOKEN_DETECTOR_NOT_WHITE_CHAR);
        Assert.assertEquals(3, tokens.size());
        Assert.assertEquals("one", byteMulti.toString(tokens.get(0), StandardCharsets.UTF_8));
        Assert.assertEquals("two", byteMulti.toString(tokens.get(1), StandardCharsets.UTF_8));
        Assert.assertEquals("three", byteMulti.toString(tokens.get(2), StandardCharsets.UTF_8));
        Assert.assertEquals(0, tokens.get(0).from());
        Assert.assertEquals(2, tokens.get(0).to());
        Assert.assertEquals(3, tokens.get(0).size());
        Assert.assertEquals(5, tokens.get(1).from());
        Assert.assertEquals(7, tokens.get(1).to());
        Assert.assertEquals(3, tokens.get(1).size());
        Assert.assertEquals(9, tokens.get(2).from());
        Assert.assertEquals(13, tokens.get(2).to());
        Assert.assertEquals(5, tokens.get(2).size());
    }

    @Test
    public void containsOnly() {
        Assert.assertTrue(byteMulti.containsOnly("one  two three ".getBytes()));
        Assert.assertFalse(byteMulti.containsOnly("one".getBytes()));
    }

    @Test
    public void writeTo() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byteMulti.writeTo(baos);
        Assert.assertEquals("one  two three ", new String(baos.toByteArray()));
    }

    @Test
    public void endWith() {
        Assert.assertTrue(byteMulti.endWith(" ".getBytes()));
        Assert.assertTrue(byteMulti.endWith("three ".getBytes()));
        Assert.assertFalse(byteMulti.endWith("three".getBytes()));
    }

}
