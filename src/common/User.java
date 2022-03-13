package common;

import java.util.ArrayList;

public class User {
    public String username;
    public String password;
    public String currentDirectory;
    public String department;
    String contact;
    String adress;
    String idNumber;
    String idValidDate;
    public boolean loggedIn;

    public User(String filename) {
        userFromFile(filename);
    }

    private void userFromFile(String filename) {
        FileHandler fh = new FileHandler();
        ArrayList<String> temp = fh.readFile(filename);

        this.username = temp.get(0);
        this.password = temp.get(1);
        this.currentDirectory = temp.get(2);
        this.department = temp.get(3);
        this.contact = temp.get(4);
        this.adress = temp.get(5);
        this.idNumber = temp.get(6);
        this.idValidDate = temp.get(7);
        this.loggedIn = false;
    }

    public String toString() {
        return this.username + " " + this.password;
    }

}
