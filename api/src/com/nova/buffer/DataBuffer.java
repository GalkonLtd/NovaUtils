package com.nova.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class DataBuffer {

    public static DataBuffer wrap(byte[] data) {
        return new DataBuffer(data);
    }

    public static DataBuffer allocate(int size) {
        return new DataBuffer(size);
    }

    private final ByteBuffer buffer;

    private DataBuffer(int size) {
        this.buffer = ByteBuffer.allocate(size);
    }

    private DataBuffer(byte[] data) {
        this.buffer = ByteBuffer.wrap(data);
    }

    public void putByte(int value) {
        this.buffer.put((byte) value);
    }

    public void putShort(int value) {
        this.buffer.putShort((short) value);
    }

    public void putMediumInt(int value) {
        this.buffer.put((byte) (value >> 16));
        this.buffer.put((byte) (value >> 8));
        this.buffer.put((byte) value);
    }

    public void putInt(int value) {
        this.buffer.putInt(value);
    }

    public void putLong(long value) {
        this.buffer.putLong(value);
    }

    public void putDouble(double value) {
        this.buffer.putDouble(value);
    }

    public void putBoolean(boolean bool) {
        this.buffer.put((byte) (bool ? 1 : 0));
    }

    public void putUTF(String string) {
        this.putString(StringType.UTF, string);
    }

    public void putString(StringType type, String string) {
        byte[] str = string.getBytes();
        if (type == StringType.UTF) {
            this.buffer.putShort((short) str.length);
            this.buffer.put(str);
        }
    }

    public void putBytes(byte[] bytes) {
        this.buffer.put(bytes);
    }

    public byte getByte() {
        return this.buffer.get();
    }

    public short getShort() {
        return this.buffer.getShort();
    }

    public int getMediumInt() {
        return ((this.buffer.get() & 0xff) << 16) + ((this.buffer.get() & 0xff) << 8) + (this.buffer.get() & 0xff);
    }

    public int getInt() {
        return this.buffer.getInt();
    }

    public long getLong() {
        return this.buffer.getLong();
    }

    public double getDouble() {
        return this.buffer.getDouble();
    }

    public boolean getBoolean() {
        return this.buffer.get() == 1;
    }

    public String getUTF() {
        return this.getString(StringType.UTF);
    }

    public String getString(StringType type) {
        if (type == StringType.UTF) {
            int length = this.buffer.getShort();
            byte[] str = new byte[length];
            this.buffer.get(str);
            return new String(str);
        }
        throw new RuntimeException("Invalid string type: " + type);
    }

    public void getBytes(byte[] dest) {
        this.buffer.get(dest);
    }

    public Buffer flip() {
        return this.buffer.flip();
    }

    public void clear() {
        this.buffer.clear();
    }

    public byte[] toByteArray() {
        return this.buffer.array();
    }

    public int size() {
        return this.buffer.array().length;
    }

    public int remaining() {
        return this.buffer.remaining();
    }

    enum StringType {
        UTF
    }

}
