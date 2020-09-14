package ir.aminer.potadoshack.client;

import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.order.Address;
import ir.aminer.potadoshack.core.order.Order;
import ir.aminer.potadoshack.core.user.BaseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseUser {
    private String jwt = null;
    private Order order;
    private final List<Address> addressList;

    public User(BaseUser user) {
        this.username = user.getUsername();
        this.first_name = user.getFirstName();
        this.last_name = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.profile_picture = user.getProfilePicture();

        addressList = new ArrayList<>();
        renewCart();
    }

    public static File getPreferenceFile() {
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

    public List<Address> getAddresses() {
        return addressList;
    }
}
