package com.thedragons.database.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A server thread created whenever a client connects to the server.
 */
public class ServerThread extends Thread {
    private Socket socket = null;

    /**
     * Constructor.
     * @param socket is the client socket.
     */
    public ServerThread(Socket socket) {
        super("FreebaseServerThread");
        this.socket = socket;
        System.out.printf(">>> %s connected%n", socket.getInetAddress());
    }

    /**
     * Handles the client connection.
     */
    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ServerProcessor serverProcessor = new ServerProcessor();
            String output = serverProcessor.processInput("");
            out.println(output);

            String input;
            while ((input = in.readLine()) != null) {
                System.out.printf(">>> %s sent: %s%n", socket.getInetAddress(), input);
                output = serverProcessor.processInput(input);
                out.println(output);
                if (output.equals("Bye")) {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
