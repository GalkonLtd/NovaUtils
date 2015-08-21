package com.nova.buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WriteBuffer extends ByteArrayOutputStream {

    public WriteBuffer() {
        super();
    }

    public void putShort(int s) {
        write(s >> 8);
        write(s);
    }

    public void putMediumInt(int s) {
        write(s >> 16);
        write(s >> 8);
        write(s);
    }

    public void putInt(int s) {
        write(s >> 24);
        write(s >> 16);
        write(s >> 8);
        write(s);
    }

    public void putLong(long l) {
        write((int) (l >> 56));
        write((int) (l >> 48));
        write((int) (l >> 40));
        write((int) (l >> 32));
        write((int) (l >> 24));
        write((int) (l >> 16));
        write((int) (l >> 8));
        write((int) l);
    }

    public void putString(String s) {
    	try {
	        write(s.getBytes());
	        write(10);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public void putUTF(String string) {
        putShort(string.getBytes().length);
        putBytes(string.getBytes());
    }

    public void putBoolean(boolean b) {
        putByte(b ? 1 : 0);
    }

	public void putSmart(int i) {
		if(i < 64 && i >= -64) {
			write(i + 64);
		} else {
			putShort(i + 49152);
		}
	}

    public void putByte(int b) {
        write(b);
    }

    public void putBytes(byte[] bytes) {
        try {
            write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putDouble(double value) {
        byte[] bytes = new byte[8];
        java.nio.ByteBuffer.wrap(bytes).putDouble(value);
        putBytes(bytes);
    }
}
