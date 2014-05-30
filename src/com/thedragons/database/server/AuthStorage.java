package com.thedragons.database.server;

import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.HashMap;

/**
 * Authentication of user accounts and storage and retrieval of save data.
 */
public class AuthStorage {

    /**
     * Validates an account name.
     * @param acctName is the account name to validate.
     * @return true if valid and false if invalid.
     * @throws IOException if buffer read failed.
     */
	private boolean validAcct(String acctName) throws IOException {
        //alphanumeric only and 2-12 characters
		if (!acctName.matches("^[a-zA-Z0-9]{2,13}$")) {

            System.out.println(">>> Account name is not valid.");
            return false;
        } else {//see if account exists
            BufferedReader in;
			String s = "";
			try {
                in = new BufferedReader(new FileReader("users.txt"));
			} catch (IOException e) {
                System.out.println(">>> Failed to write to user.txt.");
				System.out.println(e);
                return false;
			}
			in.close();
		}
		return true;
	}

    /**
     * Adds a new account.
     * @param acctName is the account name to add.
     * @param password is the password associated with the account.
     * @return true if the account was added and false if it was not added.
     * @throws IOException if file creation failed.
     */
	public boolean addAcct(String acctName, String password) throws IOException {
        //checking for if this is first user
        File u = new File("users.txt");
        if (!u.exists()) {
            try {
                u.createNewFile();
            } catch(IOException e){
                System.out.println(e);
            }
        }

		if (validAcct(acctName)) {
            if (password.contains(" ")) {
                return false;
            }
            FileWriter out;
			try {
				out = new FileWriter("users.txt", true);//1 is for append
			} catch (IOException e) {
					System.out.println(e);
                    return false;
			}
			out.write(acctName+" "+password+"\n");
			out.close();
			
			File d = new File(acctName);//making folder for users data
			d.mkdir();
            return true;
		}
        return false;
	}

    /**
     * Logs in an account.
     * @param acctName is the account name to login.
     * @param password is the password associated with the account.
     * @return true if the login succeeded and false if it failed.
     * @throws IOException if read file failed.
     */
	public boolean checkAccount(String acctName, String password) throws IOException {
        boolean isAccount = false;

        BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] credentials = line.split(" ");
            if (acctName.equals(credentials[0]) && password.equals(credentials[1])) {
                isAccount = true;
            }
        }
        reader.close();

        return isAccount;
	}

    /**
     * Gets and returns the list of saves for an account.
     * @param acctName is the account name to retrieve the saves for.
     * @return true if the save list was retrieved and false if it was not.
     * @throws IOException if read file failed.
     */
    public String getSaves(String acctName) throws IOException {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(acctName+"/saves.txt"));
        } catch (IOException e) {
            System.out.println(e);
            return "";
        }
        String s;
        String saves = "";
        while ((s = in.readLine()) != null) {
            saves += s + "-=-";
        }
        HashMap<String, String> outMap = new HashMap<>();
        outMap.put("saves", saves);
        Gson gson = new Gson();
        String output = gson.toJson(outMap);
        in.close();
        return output;
    }

    /**
     * Saves data for an account.
     * @param acctName is the account name to saves the data for.
     * @param saveName is the file name to save the data in.
     * @param content is the data to save.
     * @throws IOException if the file creation failed.
     */
	public void saveData(String acctName, String saveName, String content) throws IOException {
        File t = new File(acctName+"/"+saveName+".bin");
        if (!t.exists()) {
            FileWriter saves;
            try {
                saves = new FileWriter(acctName + "/saves.txt", true);//1 is for append
            } catch (IOException e) {
                System.out.println(e);
                return;
            }
            saves.write(saveName + "\n");
            saves.close();
        }

        FileWriter out;
		try {
			out = new FileWriter(acctName+"/"+saveName+".bin");
		} catch (IOException e) {
				System.out.println(e);
                return;
		}	
		out.write(content);
		out.close();
	}

    /**
     * Loads data for an account.
     * @param acctName is the account name to load the data for.
     * @param saveName is the file name to load the data from.
     * @return true if the data was loaded and false if it was not.
     * @throws IOException if the file was not read.
     */
	public String loadData(String acctName, String saveName) throws IOException {
        BufferedReader in;
		try {
            in = new BufferedReader(new FileReader(acctName+"/"+saveName+".bin"));
		} catch (IOException e) {
				System.out.println(e);
                return "";
		}
		String s = "";
		String outPut = "";
		while ((s = in.readLine()) != null) {
            outPut += s;
        }
		in.close();
        return outPut;
	}
}