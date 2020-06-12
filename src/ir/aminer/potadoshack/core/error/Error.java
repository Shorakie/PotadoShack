package ir.aminer.potadoshack.core.error;

public enum Error {
    WRONG_PASSWORD("Your password is incorrect", 1),
    USER_EXISTS("User with the username already exists", 2),
    UNAUTHORIZED_TOKEN("You are unauthorized", 3),
    INVALID_TOKEN("The token is invalid", 4),
    INVALID_ORDER_STATE("The order is in invalid state", 5),
    USERNAME_NOT_FOUND("User with the username doesn't exist", 6),
    ;

    private final String message;
    private final int code;

    Error(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return "(" + getCode() + ")#" + getMessage();
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }


}
