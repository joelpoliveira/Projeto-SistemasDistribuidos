package server;

import java.net.*;
import java.io.*;
import packages.json.*;
// import packages.json.JSONObject;

public class MainServer {
    public static void main(String[] args) {
        int mainPort = 0;

        if (args.length > 1) {
            System.out.println("Too many arguments. Usage: Main.java <port>");
            System.exit(1);
        }

        if (args.length < 1) {
            System.out.println("Missing arguments. Usage: Main.java <port>");
            System.exit(1);
        }

        try {
            mainPort = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Ports must be integers");
            System.exit(1);
        }

        new Server(mainPort, "MainServer", true);

        System.out.println("Main server created");

    }
    
}
