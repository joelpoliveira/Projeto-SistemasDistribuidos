package projeto;

import java.io.*;
import java.util.Scanner;

public class FileHandler {
    String filename;

    public FileHandler(String filename) {
        this.filename = filename;
    }

    public synchronized void readFile() {
        String data;
        try {
            File file = new File(this.filename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                data = reader.nextLine();
                System.out.println(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }

    public synchronized void writeFile(String text) {
        try {
            FileWriter fw = new FileWriter(this.filename, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
        }
    }

}
