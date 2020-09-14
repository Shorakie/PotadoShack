package ir.aminer.potadoshack.core.auth.simplejwt;

import java.io.Serializable;
import java.time.Instant;

public class Payload implements Serializable {
    long iat;

    public Payload() {
        iat = Instant.now().getEpochSecond();
    }
}
