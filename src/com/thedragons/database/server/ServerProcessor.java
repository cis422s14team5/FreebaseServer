package com.thedragons.database.server;

public class ServerProcessor {

    private static final int WAITING = 0;
    private static final int CONNECTED = 1;
    private static final int FILM = 2;
    private static final int TV = 3;
    private static final int SAVING = 4;

    private int state;
    private Freebase freebase;
    private TMDb tmDb;
    private FileIO io;

    public ServerProcessor() {
        freebase = new Freebase();
        tmDb = new TMDb();
        io = new FileIO();
        state = WAITING;
    }

    public String processInput(String input) {
        String output = "";

        switch (state) {
            case WAITING:
                state = CONNECTED;
                output = ("Welcome to the Freebase Movie and TV Server. Enter \"tv\" to search for TV shows or " +
                        "\"film\" to search for films.");
                break;

            case CONNECTED:
                switch (input) {
                    case "film":
                        state = FILM;
                        output = ("Enter a <film title> to search.");
                        break;
                    case "tv":
                        state = TV;
                        output = ("Enter a <tv show title> to search.");
                        break;
                    case "save":
                        state = SAVING;
                        output = ("Saving...");
                        break;
                    case "load":
                        output = io.read();
                        break;
                    default:
                        output = ("Enter \"tv\" to search for TV shows or \"film\" to search for films.");
                        break;
                }
                break;

            case FILM:
                if (input.equals("tv")) {
                    state = TV;
                    output = ("Enter a <tv show title> to search or \"film\" to search for films.");
                } else if (!(input.equals("quit") && !input.equals("tv") &&  !input.equals(""))) {
                    freebase.search(input, "film");
                    output = freebase.getTopic().toJSONString();
                } else {
                    output = ("Enter a <film title> to search or \"tv\" to search for TV shows.");
                }
                break;

            case TV:
                if (input.equals("film")) {
                    state = FILM;
                    output = ("Enter a <film title> to search or \"tv\" to search for TV shows.");
                } else if (!(input.equals("quit") && !input.equals("film") && !input.equals(""))) {
                    //freebase.search(input, "tv");
                    //output = freebase.getTopic().toJSONString();
                    output = tmDb.searchTV(input).toJSONString();
                } else {
                    output = ("Enter a <tv show title> to search or \"film\" to search for films.");
                }
                break;

            case SAVING:
                io.write(input);
                break;

            default:
                break;
        }

        if (input.equals("quit")) {
            output = ("Bye.");
            state = WAITING;
        }

        return output;
    }

}
