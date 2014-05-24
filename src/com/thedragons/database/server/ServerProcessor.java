package com.thedragons.database.server;

import java.io.IOException;
import java.util.StringTokenizer;

public class ServerProcessor {

    private static final int WAITING = 0;
    private static final int CONNECTED = 1;
    private static final int FILM = 2;
    private static final int TV = 3;
    private static final int SAVING = 4;
    private static final int GETTOPIC = 5;

    private int state;
    private Freebase freebase;
    // private TMDb tmDb;
    private FileIO io;
    private AuthStorage authStorage;

    public ServerProcessor() {
        freebase = new Freebase();
        // tmDb = new TMDb();
        io = new FileIO();
        state = WAITING;
        authStorage = new AuthStorage();
    }

    public String processInput(String input) throws IOException {
        StringTokenizer tokens = new StringTokenizer(input);
        String output = "";
        String acctName;
        String password;

        switch (state) {
            case WAITING:
                state = CONNECTED;
                output = ("Welcome to the Freebase Movie and TV Server. Enter \"tv\" to search for TV shows or " +
                        "\"film\" to search for films.");
                break;

            case CONNECTED:
                String method = tokens.nextToken();
                switch (method) {
                    case "film":
                        state = FILM;
                        output = ("Enter a <film title> to search.");
                        break;
                    case "tv":
                        state = TV;
                        output = ("Enter a <tv show title> to search.");
                        break;
                    case "getTopic":
                        state = GETTOPIC;
                        output = ("Retrieving topic...");
                        break;
                    case("add"):
                        acctName = tokens.nextToken();
                        password = tokens.nextToken();
                        if(authStorage.addAcct(acctName, password)) {
                            output = "true";
                        } else {
                            output = "false";
                        }
                        break;
                    case("login"):
                        acctName = tokens.nextToken();
                        password = tokens.nextToken();
                        if(authStorage.logIn(acctName, password)) {
                            output = "true";
                        } else {
                            output = "false";
                        }
                        break;
                    case("getsaves"):
                        acctName = tokens.nextToken();
                        output = authStorage.getSaves(acctName);
                        break;
                    case("save"):
                        acctName = tokens.nextToken();
                        String saveName = tokens.nextToken();
                        String data = "";
                        String s;
                        while ((s = tokens.nextToken()) != null) {
                            data += s + " ";
                        }

                        authStorage.saveData(acctName, saveName, data);
                        output =  "true";
                        break;
                    case("load"):
                        acctName = tokens.nextToken();
                        saveName = tokens.nextToken();
                        output =  authStorage.loadData(acctName, saveName);
                        break;
                    default:
                        output =  "unexpected input";
                        break;
                }
                break;

            case FILM:
                if (input.equals("tv")) {
                    state = TV;
                    output = ("Enter a <tv show title> to search or \"film\" to search for films.");
                } else if (!(input.equals("quit") && !input.equals("tv") && !input.equals(""))) {
                    output = freebase.search(input, "film").toJSONString();
                    // output = freebase.getTopic().toJSONString();
                } else {
                    output = ("Enter a <film title> to search or \"tv\" to search for TV shows.");
                }
                break;

            case TV:
                if (input.equals("film")) {
                    state = FILM;
                    output = ("Enter a <film title> to search or \"tv\" to search for TV shows.");
                } else if (!(input.equals("quit") && !input.equals("film") && !input.equals(""))) {
                    output = freebase.search(input, "tv").toJSONString();
                    // output = tmDb.searchTV(input).toJSONString();
                } else {
                    output = ("Enter a <tv show title> to search or \"film\" to search for films.");
                }
                break;

            case GETTOPIC:
                if (!(input.equals("quit") && !input.equals("film") && !input.equals(""))) {
                    output = freebase.getTopic(input).toJSONString();
                }
                break;

//            case SAVING:
//                io.write(input);
//                break;

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
