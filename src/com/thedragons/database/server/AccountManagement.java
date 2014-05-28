//package com.thedragons.freebase.server;
//
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class AccountManagement {
//
////login stuffs
//
//    //on creating account, check acctname is clear
////storing as [account password\n]
////only alphanumeric
//    public boolean validAcct(String acctName) throws FileNotFoundException {
//        if(!acctName.matches("^[a-zA-Z0-9]{2,13}$"))//alphanumeric only and 2-12 characters
//            return false;
//        else{//see if account exists
//            String s = "";
//            FileInputStream in = new FileInputStream("users.txt");
//            while(s = in.read() != -1){
//                if(acctName.regionMatches(0, s, 0, acctName.length())
//                return false;
//            }
//            in.close();
//        }
//        return true;
//    }
//
//    //assuming valid and unused acct name
//    public void addAcct(String acctName, String password) throws IOException {
//        FileWriter out = new FileWriter("users.txt", true);//1 is for append
//        out.write(acctName+" "+password+"\n");
//        out.close();
//    }
//
//    public boolean checkAccount(String acctName, String password) throws FileNotFoundException {
//        FileInputStream in = new FileInputStream("users.txt");
//        String s = "";
//        while(s = in.read() != -1){
//            if(acctName.regionMatches(0, s, 0, acctName.length())
//            if(password.regionMatches(acctName.length+1, s, 0, password.length())
//            return true;
//            return false;//invalid password
//        }
//        return false;//invalid account name
//    }
//
//    public void save(String acctName, String content) throws IOException {
//        FileWriter out = new FileWriter(acctName+".bin");
//        out.write(content);
//        out.close();
//    }
//
//    public String load(String acctName) throws IOException {
//        FileInputStream in = new FileInputStream(acctName+".bin");
//        String s = "";
//        String outPut = "";
//        while((s = in.read()) != null)
//            outPut += s;
//        in.close();
//    }
//
//
//
//}
