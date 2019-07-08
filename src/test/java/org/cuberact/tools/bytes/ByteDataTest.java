package org.cuberact.tools.bytes;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

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

}