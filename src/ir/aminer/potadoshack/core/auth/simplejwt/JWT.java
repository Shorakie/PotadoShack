package ir.aminer.potadoshack.core.auth.simplejwt;

import ir.aminer.potadoshack.core.utils.SerializationUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

public class JWT {

    private final Header header;
    private final Payload payload;
    private String signature;

    private JWT(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
        this.signature = null;
    }

    private JWT(Header header, Payload payload, String signature) {
        this.header = header;
        this.payload = payload;
        this.signature = signature;
    }

    public static JWT generate(Payload payload, String key) {
        return generate(new Header(), payload, key);
    }

    public static JWT generate(Header header, Payload payload, String key) {
        JWT jwt = new JWT(header, payload);
        jwt.signature = jwt.hmacSignature(key);
        return jwt;
    }

    public boolean validate(String key) {
        if (this.signature == null)
            throw new IllegalStateException("Signature is null");

        if (!hmacSignature(key).equals(this.signature))
            return false;

        return Instant.now().isBefore(Instant.ofEpochSecond(payload.iat).plus(Duration.ofDays(7)));
    }

    public String hmacSignature(String key) {
        Mac hmac = null;
        try {
            hmac = Mac.getInstance(header.alg);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println(header.alg + " hmac algorithm was not found. using HmacSHA256.");
            try {
                hmac = Mac.getInstance("HmacSHA256");
            } catch (NoSuchAlgorithmException e) {
                return null;
            }
        }
        try {
            hmac.init(new SecretKeySpec(key.getBytes(), header.alg));
        } catch (InvalidKeyException e) {
            System.err.println("Key is invalid.");
            return null;
        }

        return Base64.getUrlEncoder().encodeToString(hmac.doFinal(getEncodedHeaderPayload().getBytes()));
    }

    public static JWT decode(String jwtString) {
        String[] jwtParts = jwtString.split("\\.");
        if (jwtParts.length != 3)
            throw new IllegalArgumentException("Should be valid jwt");

        Header header = SerializationUtils.deserialize(Base64.getUrlDecoder().decode(jwtParts[0]));
        Payload payload = SerializationUtils.deserialize(Base64.getUrlDecoder().decode(jwtParts[1]));

        return new JWT(header, payload, jwtParts[2]);
    }

    public String encode(String key) {
        return getEncodedHeaderPayload() + "." + hmacSignature(key);
    }

    private String getEncodedHeaderPayload() {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(header))
                + "." + Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(payload));
    }

    public Header getHeader() {
        return header;
    }

    public <P extends Payload> P getPayload() {
        return (P) payload;
    }

    @Override
    public String toString() {
        return getEncodedHeaderPayload() + "." + signature;
    }
}
