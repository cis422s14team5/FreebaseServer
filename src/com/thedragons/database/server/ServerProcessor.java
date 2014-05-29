package com.thedragons.database.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Process input for the server.
 */
public class ServerProcessor {

    private static final int WAITING = 0;
    private static final int CONNECTED = 1;
    private static final int FILM = 2;
    private static final int TV = 3;
    private static final int SAVING = 4;
    private static final int GETTOPIC = 5;
    private static final int ADDACCOUNT = 6;

    private int state;
    private Freebase freebase;
    // private TMDb tmDb;
    private FileIO io;
    private AuthStorage authStorage;
    private Gson gson;

    private ArrayList<String> loggedInList;

    /**
     * Constructor.
     */
    public ServerProcessor() {
        freebase = new Freebase();
        // tmDb = new TMDb();
        io = new FileIO();
        state = WAITING;
        authStorage = new AuthStorage();
        loggedInList = new ArrayList<>();
        gson = new Gson();
    }

    /**
     * Processes server input and generates output to be sent to a client.
     * @param input is the input to process.
     * @return the output.
     * @throws IOException if authentication, saving, or loading failed.
     */
    public String processInput(String input) throws IOException {
        //StringTokenizer tokens = new StringTokenizer(input);
        String[] inputArray = input.split("-=-");
        String output = "";
        String acctName;
        String password;
        String saveName;

        switch (state) {
            case WAITING:
                state = CONNECTED;
                output = ("Welcome to the WatchlistPro Server.");
                break;

            case CONNECTED:
                // String method = tokens.nextToken();
                System.out.println(">>> Processing: " + inputArray[0]);
                switch (inputArray[0]) {
                    case "film":
                        state = FILM;
                        output = ("Processing: " + inputArray[0]);
                        System.out.println(">>> Ready to search for films.");
                        break;
                    case "tv":
                        state = TV;
                        output = ("Processing: " + inputArray[0]);
                        System.out.println(">>> Ready to search for TV shows.");
                        break;
                    case "getTopic":
                        state = GETTOPIC;
                        output = ("Retrieving topic...");
                        System.out.println(">>> Retrieving topic.");
                        break;
                    case("add"):
                        state = ADDACCOUNT;
                        System.out.println(">>> Sending public RSA key.");
                        EncryptionUtil cipher = new EncryptionUtil();
                        if (!cipher.areKeysPresent()) {
                            cipher.generateKey();
                        }
                        ObjectInputStream inputStream = new ObjectInputStream(
                                new FileInputStream(cipher.getPublicKeyFile()));
                        PublicKey publicKey;
                        try {
                            publicKey = (PublicKey) inputStream.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        HashMap<String, PublicKey> keyMap = new HashMap<>();
                        keyMap.put("key", publicKey);
                        output = gson.toJson(keyMap);
                        break;
                    case("login"):
                        acctName = inputArray[1];
                        password = inputArray[2];
                        System.out.println(">>> Attempting to login account: " + acctName);
                        if (authStorage.checkAccount(acctName, password)) {
                            System.out.println(">>> Logged in account: " + acctName);
                            //loggedInList.add(acctName);
                            output = "true";
                        } else {
                            System.out.println(">>> Failed to login account: " + acctName);
                            output = "false";
                        }
                        break;
                    case("logout"):
                        acctName = inputArray[1];
                        password = inputArray[2];
                        System.out.println(">>> Attempting to logout account: " + acctName);
                        if (authStorage.checkAccount(acctName, password)) {
                            System.out.println(">>> Successfully logged out account: " + acctName);
                            //loggedInList.remove(acctName);
                            output = "true";
                        } else {
                            System.out.println(">>> Failed to logged out account: " + acctName);
                            output = "false";
                        }
                        break;
                    case("getsaves"):
                        if (inputArray.length > 1) {
                            acctName = inputArray[1];
                            System.out.println(">>> Getting saves for: " + acctName);
                            output = authStorage.getSaves(acctName);
                            System.out.println(">>> Sending: " + output);
//                            if (loggedInList.contains(inputArray[1])) {
//                                acctName = inputArray[1];
//                                System.out.println(">>> Getting saves for: " + acctName);
//                                output = authStorage.getSaves(acctName);
//                                System.out.println(">>> Sending: " + output);
//                            } else {
//                                System.out.println(">>> Failed to get saves because user not logged in.");
//                                output = "User account is not logged in.";
//                            }
                        } else {
                            System.out.println(">>> Failed to get saves because user not logged in.");
                            output = "User account is not logged in.";

                        }
                        break;
                    case("save"):
                        if (inputArray.length > 1) {
                            acctName = inputArray[1];
                            saveName = inputArray[2];
                            String data = inputArray[3];
                            System.out.println(">>> Saving data for: " + acctName);
                            authStorage.saveData(acctName, saveName, data);
                            output = "true";
                        } else {
                            System.out.println(">>> Failed to save because user not logged in.");
                            output = "User account is not logged in.";

                        }
                        break;
                    case("load"):
                        acctName = inputArray[1];
                        saveName = inputArray[2];
                        System.out.println(">>> Loading data for: " + acctName);
                        output = authStorage.loadData(acctName, saveName);
                        System.out.println(">>> Sending: " + output);
                        break;
                    default:
                        output = "Server received unexpected input: " + inputArray[0];
                        break;
                }
                break;

            case FILM:
                if (input.equals("tv")) {
                    state = TV;
                    output = ("Server received: " + input);
                } else if (!(input.equals("quit") && !input.equals("tv") && !input.equals(""))) {
                    output = freebase.search(input, "film").toJSONString();
                    // output = freebase.getTopic().toJSONString();
                } else {
                    output = ("Server received: " + input);
                }
                break;

            case TV:
                if (input.equals("film")) {
                    state = FILM;
                    output = ("Server received: " + input);
                } else if (!(input.equals("quit") && !input.equals("film") && !input.equals(""))) {
                    output = freebase.search(input, "tv").toJSONString();
                    // output = tmDb.searchTV(input).toJSONString();
                } else {
                    output = ("Server received: " + input);
                }
                break;

            case GETTOPIC:
                if (!(input.equals("quit") && !input.equals("film") && !input.equals(""))) {
                    output = freebase.getTopic(input).toJSONString();
                }
                break;

            case ADDACCOUNT:
                if (!(input.equals("quit") && !input.equals("film") && !input.equals(""))) {
                    Type mapType = new TypeToken<HashMap<String, String>>(){}.getType();
                    HashMap<String, String> inputMap = gson.fromJson(input, mapType);
                    acctName = inputMap.get("username");
                    password = inputMap.get("password");
                    System.out.println(">>> Attempting to add account: " + acctName);
                    if (authStorage.addAcct(acctName, password)) {
                        System.out.println(">>> Added new account: " + acctName);
                        output = "true";
                    } else {
                        System.out.println(">>> Failed to add new account: " + acctName);
                        output = "false";
                    }
                }

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
