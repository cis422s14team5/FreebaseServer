package com.thedragons.database.server;

import java.io.*;
import java.net.*;


public class AuthStorage {

//    public static void main(String [] args) {
//        AuthStorage as = new AuthStorage();
//        try {
//            as.addAcct("ac1", "pw1");
//            as.addAcct("ac2", "pw2");
//            as.addAcct("ac1", "pw3");
//            as.addAcct("ac5", "pw5");
//
//            as.logIn("ac1", "pw1");
//            as.logIn("ac1", "pw2");
//
//            as.saveData("ac1", "save data for ac1", "save1");
//            as.saveData("ac1", "save data for ac1", "save2");
//            as.saveData("ac1", "save data for ac1", "save3");
//            as.saveData("ac1", "save data for ac1", "save4");
//
//            System.out.println(as.loadData("ac1", "save1"));
//            System.out.println(as.getSaves("ac1"));
//        }catch (IOException e) {
//            System.out.println(e);
//        }
//    }

	//only alphanumeric
	private boolean validAcct(String acctName) throws IOException {
        //alphanumeric only and 2-12 characters
		if (!acctName.matches("^[a-zA-Z0-9]{2,13}$")) {
            return false;
        } else {//see if account exists
            BufferedReader in;
			String s = "";
			try {
                in = new BufferedReader(new FileReader("users.txt"));
			} catch (IOException e) {
				System.out.println(e);
                return false;
			}
			while((s = in.readLine()) != null){
				if (acctName.regionMatches(0, s, 0, acctName.length()))
					return false;
			}
			in.close();
		}
		return true;
	}


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

		if (validAcct(acctName)){
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

	public boolean logIn(String acctName, String password) throws IOException {
        BufferedReader in;
		try {
            in = new BufferedReader(new FileReader("users.txt"));
		} catch (IOException e) {
				System.out.println(e);
                return false;
		}
        String s = "";
        if ((s = in.readLine()) != null) {
            if (acctName.regionMatches(0, s, 0, acctName.length())) {
                return password.regionMatches(acctName.length() + 1, s, 0, password.length());
            }

        }
        return false;//invalid account name
	}

    public String getSaves(String acctName) throws IOException {
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(acctName+"/saves.txt"));
        } catch (IOException e) {
            System.out.println(e);
            return "";
        }
        String s = "";
        String outPut = "";
        while ((s = in.readLine()) != null) {
            outPut += s + "-=-";
        }
        in.close();
        return outPut;
    }

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