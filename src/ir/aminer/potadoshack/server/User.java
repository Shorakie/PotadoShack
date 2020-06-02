package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.core.BaseUser;
import ir.aminer.potadoshack.core.auth.simplejwt.UserPayload;
import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.order.Order;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class User extends BaseUser {

    protected String password;
    protected HashMap<Integer, Order> orders;

    public User(String username, String password, String first_name, String last_name) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public static User fromUsername(String username) {
        return (User) loadUser(new File("./clients/" + username + ".pref"));
    }

    public static User fromJWT(JWT jwt) {
        if (!(jwt.getPayload() instanceof UserPayload))
            throw new IllegalArgumentException("JWT payload is not a UserPayload");

        UserPayload userPayload = jwt.getPayload();
        return fromUsername(userPayload.getUsername());
    }

    public void addOrder(Order order) {
        orders.put(order.getCode(), order);
    }

    public Order getOrder(int code) {
        return orders.get(code);
    }

    public void removeOrder(int code) {
        orders.remove(code);
    }

    public String getPassword() {
        return password;
    }
}
