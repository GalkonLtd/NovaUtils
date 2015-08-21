package com.nova.file;

import com.nova.buffer.DataBuffer;

import java.io.*;

/**
 * "The real danger is not that computers will begin to think like men, but that men will begin to think like computers." – Sydney Harris
 * Created on 7/21/2015
 */
public final class FileUtils {

    /**
     * Returns whether or not the file at the specified path exists.
     * @param path  the file path
     * @return  <i>true</i> if path exists
     */
    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

    /**
     * Returns whether or not the specified path is a directory.
     * @param path  the file path
     * @return  <i>true</i> if path is directory
     */
    public static boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }

    /**
     * Returns the file at the specified path as a byte array.
     * @param path
     * @return
     */
    public static byte[] fileToByteArray(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        FileInputStream in = null;
        try {
            byte[] array = new byte[(int) file.length()];
            in = new FileInputStream(file);
            in.read(array);
            return array;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Returns the given {@link java.io.InputStream} as a byte array.
     * @param ins
     * @return
     */
    public static byte[] inputStreamToByteArray(InputStream ins) {
        byte[] availableBytes = new byte[0];
        try {
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream outs = new ByteArrayOutputStream();

            int read = 0;
            while ((read = ins.read(buffer)) != -1) {
                outs.write(buffer, 0, read);
            }

            ins.close();
            outs.close();
            availableBytes = outs.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return availableBytes;
    }

    /**
     * Returns the file at the specified resource path as a byte array. This is to be used for packaged assets (e.g. com.example.resources.MyImage.png).
     * @param resourcePath
     * @return
     */
    public static byte[] resourceToByteArray(String resourcePath) {
        return FileUtils.inputStreamToByteArray(FileUtils.class.getResourceAsStream(resourcePath));
    }

    /**
     * Writes data to the specified file path.
     *
     * @param path
     * @param data
     */
    public static void writeToFile(String path, byte[] data) {
        if (data == null) {
            throw new RuntimeException("File data is null!");
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(data);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the file at the specified path as a {@link DataBuffer}.
     * @param path
     * @return
     */
    public static DataBuffer fileToDataBuffer(String path) {
        return DataBuffer.wrap(fileToByteArray(path));
    }

}
