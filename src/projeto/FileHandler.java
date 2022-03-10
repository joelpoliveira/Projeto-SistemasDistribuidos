package projeto;

import java.io.*;
import java.util.Scanner;
import java.util.HashMap;

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

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getCredentials(){
        HashMap<String, String> teste = null;

        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream("credentials"));
            teste = (HashMap<String, String>) i.readObject();
            i.close();

        } catch (IOException e) {
            System.err.println("Credentials file doesnt exist");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro 2");
        }

        return teste;

    }

}
