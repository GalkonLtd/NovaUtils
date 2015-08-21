package com.nova.file;

import com.nova.buffer.DataBuffer;
import com.nova.buffer.WriteBuffer;

import java.io.IOException;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 8/20/2015
 */
public abstract class ReadWriteFile {

    public String getFilePath() {
        return filePath;
    }

    private final String filePath;

    public ReadWriteFile(String filePath) {
        this.filePath = filePath;
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
        FileUtils.writeToFile(this.filePath, buffer.toByteArray());
        return true;
    }

    public abstract void readFromBuffer(DataBuffer buffer);

    public boolean read() {
        if (!FileUtils.fileExists(this.filePath)) {
            System.err.print(this.filePath + " does not exist!");
            return false;
        }
        DataBuffer buffer = DataBuffer.wrap(FileUtils.fileToByteArray(this.filePath));
        this.readFromBuffer(buffer);
        return true;
    }

}