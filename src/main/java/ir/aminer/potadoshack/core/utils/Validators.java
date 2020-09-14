package ir.aminer.potadoshack.core.utils;

import java.util.function.Consumer;

public class Validators {
    public static boolean passwordFieldValidator(String password, Consumer<String> error) {
        if (password.isEmpty()) {
            error.accept("This field is required.");
            return false;
        } else if (password.length() < 8) {
            error.accept("Password should at least be 8 characters.");
            return false;
        } else if (password.length() > 16) {
            error.accept("Password should at most be 16 characters.");
            return false;
        } else if (!password.matches("^([a-zA-Z0-9@*#_]{8,15})$")) {
            error.accept("Password should be consist of alphabet, numbers and @#*_");
            return false;
        }
        return true;
    }

}
