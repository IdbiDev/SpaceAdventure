package me.idbi.spaceadventure.debug;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Debug {
    private static Process process;
    private static PrintWriter writer;
    private static Socket socket;
    private static ServerSocket serverSocket;
    private static final int DEBUG_PORT = 12345; // Choose any available port

    public static void initDebug() {
        try {
            // Start server socket first
            serverSocket = new ServerSocket(DEBUG_PORT);

            // Launch the secondary console without the /k flag so it doesn't keep the window open when the process ends
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "cmd",
                    "/c",
                    "start",
                    "cmd",
                    "/k",
                    "java -cp ./SpaceAdventure-1.0.0-me.idbi.spaceadventure.jar me.idbi.spaceadventure.debug.SecondaryConsole " + DEBUG_PORT
            );

            // Start the process
            process = processBuilder.start();

            // Wait for the secondary console to connect
            System.out.println("Waiting for debug console to connect...");
            socket = serverSocket.accept();
            System.out.println("Debug console connected!");

            // Create a writer to send messages
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Send an initial test message
            writer.println("Debug console initialized successfully");
        } catch (IOException e) {
            System.err.println("Failed to initialize debug console:");
            e.printStackTrace();
        }
    }

    public static void printDebug(String msg) {
        if (writer != null) {
            writer.println(msg);
            // PrintWriter with autoFlush=true will flush automatically
        } else {
            System.err.println("Debug console not initialized. Message: " + msg);
        }
    }

    public static void closeDebug() {
        if (writer != null) {
            writer.println("EXIT_DEBUG_CONSOLE");
            writer.close();
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing debug connections: " + e.getMessage());
        }

        if (process != null) {
            process.destroy();
        }
    }
}
