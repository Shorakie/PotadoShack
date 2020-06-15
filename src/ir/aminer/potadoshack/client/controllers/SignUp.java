package ir.aminer.potadoshack.client.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.Packet;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.network.packets.SignUpPacket;
import ir.aminer.potadoshack.core.utils.AnimationUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignUp extends Page {
    public TextField txt_username;
    public PasswordField txt_password;
    public PasswordField txt_password_confirm;
    public TextField txt_first_name;
    public TextField txt_last_name;
    public AnchorPane body;

    private JFXSnackbar snackbar;

    public SignUp() {
        super("layouts/SignUp.fxml");
    }

    public void initialize() {
        snackbar = new JFXSnackbar(body);

        txt_username.setTextFormatter(new TextFormatter<Object>(change -> {
            if (change.getControlNewText().isEmpty())
                return change;

            if (!change.getControlNewText().matches("^(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+$"))
                return null;

            return change;
        }));
    }

    public void signup(ActionEvent actionEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if (txt_username.getLength() < 6) {
            AnimationUtils.pulse(txt_username).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Username should at least be 6 characters.")));
            return;
        }

        if (txt_password.getText().isEmpty()) {
            AnimationUtils.pulse(txt_password).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("This field is required.")));
            return;
        } else if (txt_password.getLength() < 8) {
            AnimationUtils.pulse(txt_password).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Password should at least be 8 characters.")));
            return;
        } else if (txt_password.getLength() > 16) {
            AnimationUtils.pulse(txt_password).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Password should at most be 16 characters.")));
            return;
        } else if (!txt_password.getText().matches("^([a-zA-Z0-9@*#_]{8,15})$")){
            AnimationUtils.pulse(txt_password).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Password should be consist of alphabet, numbers and @#*_")));
            return;
        } else if (!txt_password.getText().equals(txt_password_confirm.getText())) {
            AnimationUtils.pulse(txt_password_confirm).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Passwords doesn't match.")));
            return;
        }

        if (txt_first_name.getText().isEmpty()) {
            AnimationUtils.pulse(txt_first_name).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("This field is required.")));
            return;
        }

        ClientSocket client = new ClientSocket(Main.host, Main.port);

        Packet signup = new SignUpPacket(
                txt_username.getText(),
                txt_password.getText(),
                txt_first_name.getText(),
                txt_last_name.getText());

        client.sendPacket(signup);

        handleResponse(client);
    }

    @Override
    public void onResponse(ResponsePacket response) {
        User user = new User(txt_username.getText(), txt_first_name.getText(), txt_last_name.getText());

        if (!User.getPreferenceFile().exists()) {
            try {
                User.getPreferenceFile().createNewFile();
            } catch (IOException ioException) {
                System.err.println("Couldn't create preference file");
            }
        }

        user.setJwt(((PrimitivePacket<String>) response.getResponse()).getData());
        user.save();
        PageHandler.getInstance().activePage("sign_in");
    }

    @Override
    public void onError(Error error) {
        if (error.equals(Error.USER_EXISTS)) {
            AnimationUtils.pulse(txt_username).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("A user with this username already exists.")));
        }
    }

    public void Signin(ActionEvent actionEvent) {
        ((Hyperlink) actionEvent.getSource()).setVisited(false);
        PageHandler.getInstance().activePage("sign_in");
    }
}
