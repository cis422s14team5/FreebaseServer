package com.thedragons.database.server;

import java.io.*;
import java.util.LinkedList;


public class ByteArrayHandler {

    /**
     * Write to byte array.
     * @param list is the list to write.
     * @return a byte array.
     */
    public byte[] writeByteArray(LinkedList<String> list) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);
        list.forEach((t) -> {
            try {
                out.writeUTF(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return byteArray.toByteArray();
    }

    /**
     * Read from byte array.
     * @param bytes is the array of bytes to read.
     * @return a list of strings.
     */
    public LinkedList<String> readByteArray(byte[] bytes) {
        ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
        DataInputStream in = new DataInputStream(byteArray);
        LinkedList<String> list = new LinkedList<>();
        try {
            while (in.available() > 0) {
                String element = in.readUTF();
                list.add(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
