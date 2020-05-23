package ir.aminer.potadoshack.core.auth.simplejwt;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Header implements Serializable {
    String alg;

    public Header() {
        this("HmacSHA256");
    }

    public Header(String alg) {
        this.alg = alg;
    }
}