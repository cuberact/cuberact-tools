package org.cuberact.tools.bytes;

public class ByteMulti extends ABytes {

    private Bytes[] multi;
    private int size;

    public ByteMulti(Bytes... bytes) {
        this.multi = bytes;
        for (Bytes bd : bytes) {
            this.size += bd.size();
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public byte get(int index) {
        for (int i = 0; i < multi.length; i++) {
            if (index < multi[i].size()) return multi[i].get(index);
            index -= multi[i].size();
        }
        return 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public byte[] toArray() {
        byte[] bytes = new byte[size];
        int i = 0;
        for (Bytes byteData : multi) {
            byte[] sub = byteData.toArray();
            if (sub.length == 0) continue;
            System.arraycopy(sub, 0, bytes, i, sub.length);
            i += sub.length;
        }
        return bytes;
    }

}
