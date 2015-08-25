package com.nova.file;

import com.nova.buffer.DataBuffer;
import com.nova.buffer.WriteBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/20/2015
 */
public abstract class ReadWriteFile {

    public String getFilePath() {
        return filePath;
    }

    private final String filePath;
    private RandomAccessFile randomAccessFile;

    public ReadWriteFile(String filePath) {
        this.filePath = filePath;
        try {
            this.randomAccessFile = new RandomAccessFile(filePath, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public abstract void writeToBuffer(WriteBuffer buffer);

    public boolean write() {
        WriteBuffer buffer = new WriteBuffer();
        this.writeToBuffer(buffer);
        try {
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            this.randomAccessFile.seek(0);
            this.randomAccessFile.write(buffer.toByteArray());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileUtils.writeToFile(this.filePath, buffer.toByteArray());
        return true;
    }

    public abstract void readFromBuffer(DataBuffer buffer);

    public boolean read() {
        if (!FileUtils.fileExists(this.filePath)) {
            System.err.print(this.filePath + " does not exist!");
            return false;
        }
        try {
            byte[] data = new byte[(int) new File(this.filePath).length()];
            this.randomAccessFile.seek(0);
            this.randomAccessFile.readFully(data);
            DataBuffer buffer = DataBuffer.wrap(data);
            try {
                this.readFromBuffer(buffer);
                return true;
            } catch (BufferUnderflowException e) {
                //ignore
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataBuffer buffer = DataBuffer.wrap(FileUtils.fileToByteArray(this.filePath));
        try {
            this.readFromBuffer(buffer);
            return true;
        } catch (BufferUnderflowException e) {
            //ignore
        }
        return false;
    }

    public void close() {
        try {
            this.randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}