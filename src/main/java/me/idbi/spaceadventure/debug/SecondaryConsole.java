package me.idbi.spaceadventure.debug;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SecondaryConsole {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java SecondaryConsole <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        Socket socket = null;
        BufferedReader reader = null;

        try {
            // Print startup header
            System.out.println("Debug Console Started");
            System.out.println("=====================");
            System.out.println("Connecting to main application on port " + port + "...");

            // Connect to the main application
            socket = new Socket("localhost", port);
            System.out.println("Connected to main application!");

            // Create a reader for incoming messages
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Date formatter for timestamps
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

            // Process messages from the main application
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("EXIT_DEBUG_CONSOLE")) {
                    System.out.println("Shutting down debug console...");
                    break;
                }

                // Add timestamp and print the message
                String timestamp = dateFormat.format(new Date());
                System.out.println("[" + timestamp + "] " + line);
            }
        } catch (Exception e) {
            System.err.println("Error in debug console: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Clean up resources
            try {
                if (reader != null) reader.close();
                if (socket != null) socket.close();

                System.out.println("Debug console terminated.");
            } catch (Exception e) {
                // Ignore close errors
            }
        }
    }
}
