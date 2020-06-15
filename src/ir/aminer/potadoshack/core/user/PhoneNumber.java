package ir.aminer.potadoshack.core.user;

import java.io.Serializable;

public class PhoneNumber implements Serializable {
    private final int code;
    private final long number;

    public PhoneNumber(int code, long number) {
        this.code = code;
        this.number = number;
    }

    public int getCode() {
        return code;
    }

    public long getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "+"+code+""+number;
    }
}
