package ir.aminer.potadoshack.client;

import ir.aminer.potadoshack.core.BaseClient;

import java.io.File;

public class Client extends BaseClient {
    String jwt;

    public static File getPreferenceFile(){
        return new File("./user.pref");
    }

    public static BaseClient loadClient() {
        if (!hasPreference())
            throw new IllegalStateException("Client doesn't have a preference.");
        return loadClient(getPreferenceFile());
    }

    public static boolean hasPreference() {
        File credentialsFile = getPreferenceFile();
        return (credentialsFile.exists() && credentialsFile.isFile());
    }
}
