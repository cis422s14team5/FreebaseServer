package com.thedragons.database.server;

import java.io.*;

/**
 * Reads and writes to the file system. Used to read and write to the store.txt file.
 */
public class FileIO {

    /**
     * Writes the string to the file "store.txt".
     */
    public void write(String string) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("store.txt")));
            writer.write(string);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads from the file "store.txt" into a string.
     * @return a string representation of "store.txt".
     */
    public String read() {
        String output = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader("store.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                output = line;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
