package common;

import java.io.*;
import java.util.*;

public class FileHandler {

    public FileHandler() {}

    public synchronized ArrayList<String> readFile(String filename) {
        ArrayList<String> text = new ArrayList<String>();
        try {
            File file = new File(filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                text.add(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return text;
    }

    // Not a very good option to rewrite the entire file :)
    public synchronized void reWriteFile(String filename, ArrayList<String> lines) {
        try {
            FileWriter fw = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String s : lines) {
                bw.write(s);
                bw.newLine();   
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

    public void writeLine(String filename, String s) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

}
