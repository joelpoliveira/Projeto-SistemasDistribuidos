package server;

import java.net.*;
import java.io.*;

public class SecondaryServer {
    public static void main(String[] args) {
        int secondaryPort = 0;

        if (args.length > 1) {
            System.out.println("Too many arguments. Usage: Main.java <port>");
            System.exit(1);
        }

        if (args.length < 1) {
            System.out.println("Missing arguments. Usage: Main.java <port>");
            System.exit(1);
        }

        try {
            secondaryPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ports must be integers");
            System.exit(1);
        }

        new Server(secondaryPort, "SecondaryServer");

        System.out.println("Secondary server created");

    }
    
}
