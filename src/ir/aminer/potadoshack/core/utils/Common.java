package ir.aminer.potadoshack.core.utils;

import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.error.Error;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.function.Consumer;

public class Common {
    public static String hmacSha256(String key, String raw){
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

    public static Consumer<Error> getAuthorityErrorChecker() {
        return error -> {
            if (error.equals(Error.UNAUTHORIZED_TOKEN) || error.equals(Error.INVALID_TOKEN))
                if (User.getPreferenceFile().delete())
                    PageHandler.getInstance().activePage("sign_in");
        };
    }
}
