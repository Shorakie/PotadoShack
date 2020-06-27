package ir.aminer.potadoshack.server;

import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.auth.simplejwt.UserPayload;
import ir.aminer.potadoshack.core.order.Order;
import ir.aminer.potadoshack.core.user.BaseUser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class User extends BaseUser {

    protected String password;
    protected HashMap<Integer, Order> orders = new HashMap<>();

    public User(String username, String password, String first_name, String last_name) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        try {
            this.profile_picture = ImageIO.read(getClass().getResource("/default-user.jpg"));
        } catch (IOException ignored) {
            this.profile_picture = null;
            System.err.println("Couldn't read image");
        }
    }

    public static boolean hasUserFilePref(String username) {
        File credentialsFile = getUserFilePref(username);
        return (credentialsFile.exists() && credentialsFile.isFile());
    }

    public static File getUserFilePref(String username) {
        return new File("./clients/" + username + ".pref");
    }

    public static User fromUsername(String username) {
        if (!hasUserFilePref(username))
            throw new IllegalStateException("User doesn't have a preference");

        return (User) loadUser(getUserFilePref(username));
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

    public HashMap<Integer, Order> getOrders() {
        return orders;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean save() {
        return super.save(getUserFilePref(username));
    }
}
