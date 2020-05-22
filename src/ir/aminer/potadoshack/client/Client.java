package ir.aminer.potadoshack.client;

import java.io.File;
import java.io.Serializable;

public class Client implements Serializable {
    String username;
    String first_name;
    String last_name;

    public static boolean hasPreference(){
        File credentialsFile = new File("./user.pref");
        return (credentialsFile.exists() && credentialsFile.isFile());
    }

    public boolean isAuthenticated(){
        return false;
    }
}
