package ir.aminer.potadoshack.client;

import ir.aminer.potadoshack.core.BaseUser;
import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.order.Cart;
import ir.aminer.potadoshack.core.order.Order;

import java.io.File;

public class User extends BaseUser {
    private String jwt = null;
    private Order order;

    public User(String username, String firstName, String lastName) {
        this.username = username;
        this.first_name = firstName;
        this.last_name = lastName;
        renewCart();
    }

    public static File getPreferenceFile(){
        return new File("./user.pref");
    }

    public static User loadClient() {
        if (!hasPreference())
            throw new IllegalStateException("Client doesn't have a preference.");
        return (User) loadUser(getPreferenceFile());
    }

    public static boolean hasPreference() {
        File credentialsFile = getPreferenceFile();
        return (credentialsFile.exists() && credentialsFile.isFile());
    }

    public boolean save() {
        return super.save(getPreferenceFile());
    }

    public JWT getJwt() {
        return JWT.decode(jwt);
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Order getOrder() {
        return this.order;
    }

    public void renewCart() {
        this.order = new Order();
    }
}
