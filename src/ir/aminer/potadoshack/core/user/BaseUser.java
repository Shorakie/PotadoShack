package ir.aminer.potadoshack.core.user;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class BaseUser implements Serializable {
    protected String username;
    protected String first_name;
    protected String last_name;
    protected String email;

    protected PhoneNumber phoneNumber;

    /* SO https://stackoverflow.com/questions/33074774/javafx-image-serialization */
    protected transient BufferedImage profile_picture;

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        profile_picture = ImageIO.read(stream);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        ImageIO.write(profile_picture, "png", stream);
    }

    public BaseUser() {
        try {
            this.profile_picture = ImageIO.read(getClass().getResource("/default-user.jpg"));
        } catch (IOException ignored) {
            System.err.println("Couldn't read the image");
            this.profile_picture = null;
        }
    }

    public static BaseUser loadUser(File userFile) {
        if (!(userFile.exists() && userFile.isFile()))
            throw new IllegalStateException("Specified path should exist and be a valid file.");

        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(userFile));
        } catch (IOException e) {
            System.err.println("Could not initialize InputStream.");
            return null;
        }

        Object o;
        try {
            o = inputStream.readObject();
        } catch (IOException e) {
            System.err.println("Could not read the user.");
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("User class was not found.");
            return null;
        }

        /* Close the stream */
        try {
            inputStream.close();
        } catch (IOException e) {
            System.err.println("Could not close the stream.");
        }

        return (BaseUser) o;
    }

    public boolean save(File userFile) {
        if (!(userFile.exists() && userFile.isFile()))
            throw new IllegalStateException("Specified path should exist and be a valid file.");

        ObjectOutputStream outputStream;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(userFile));
        } catch (IOException e) {
            System.err.println("Could not initialize OutputStream.");
            return false;
        }

        try {
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.err.println("Could not write the user.");
            return false;
        }

        /* Close the stream */
        try {
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Could not close the stream.");
        }

        return true;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BufferedImage getProfilePicture() {
        return profile_picture;
    }

    public void setProfilePicture(BufferedImage profile_picture) {
        this.profile_picture = profile_picture;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }
}
