package org.cuberact.tools.bytes;

public class ByteMulti extends ABytes {

    private Bytes[] data;
    private int size;

    public ByteMulti(Bytes... bytes) {
        this.data = bytes;
        for (Bytes bd : bytes) {
            this.size += bd.size();
        }
    }

    @Override
    public byte get(int index) {
        for (Bytes byteData : data) {
            if (index < byteData.size()) return byteData.get(index);
            index -= byteData.size();
        }
        return 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public byte[] toArray() {
        ByteData sum = new ByteData();
        for (Bytes byteData : data) {
            sum.add(byteData);
        }
        return sum.toArray();
    }

}
