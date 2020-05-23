package ir.aminer.potadoshack.core.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HmacSha256 {
    public static String hash(String key, String raw){
        Mac hmac = null;
        try {
            hmac = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
        try {
            hmac.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
        } catch (InvalidKeyException e) {
            System.err.println("Key is invalid.");
            return null;
        }

        return Base64.getUrlEncoder().encodeToString(hmac.doFinal(raw.getBytes()));
    }
}
