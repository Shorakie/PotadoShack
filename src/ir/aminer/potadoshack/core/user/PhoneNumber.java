package ir.aminer.potadoshack.core.user;

import java.io.Serializable;

public class PhoneNumber implements Serializable {
    private final int code;
    private final int number;

    public PhoneNumber(int code, int number) {
        this.code = code;
        this.number = number;
    }

    public int getCode() {
        return code;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "+"+code+""+number;
    }
}
