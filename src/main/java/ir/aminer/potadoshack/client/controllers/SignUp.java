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
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.network.packets.SignUpPacket;
import ir.aminer.potadoshack.core.network.packets.UserPacket;
import ir.aminer.potadoshack.core.utils.AnimationUtils;
import ir.aminer.potadoshack.core.utils.Validators;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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

    @FXML
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

    public void signup(ActionEvent actionEvent) throws IOException {
        if (txt_username.getLength() < 6) {
            AnimationUtils.pulse(txt_username).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Username should at least be 6 characters.")));
            return;
        }

        if (!Validators.passwordFieldValidator(txt_password.getText(), error -> {
            AnimationUtils.pulse(txt_password).play();
            snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(error)));
        }))
            return;
        else if (!txt_password.getText().equals(txt_password_confirm.getText())) {
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
        UserPacket packet;
        if (response.getResponse() instanceof UserPacket)
            packet = (UserPacket) response.getResponse();
        else
            return;

        User user = new User(packet.getUser());

        if (!User.getPreferenceFile().exists()) {
            try {
                User.getPreferenceFile().createNewFile();
            } catch (IOException ioException) {
                System.err.println("Couldn't create preference file");
            }
        }

        user.setJwt(packet.getJwt());
        user.save();
        PageHandler.getInstance().activePage("sign_in");
    }

    @Override
    public void onError(Error error) {
        JFXSnackbarLayout layout = new JFXSnackbarLayout("An error occurred!");
        layout.getStyleClass().add("error");

        if (error.equals(Error.USER_EXISTS)) {
            AnimationUtils.pulse(txt_username).play();
            layout.setToast("A user with this username already exists.");
        }

        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(layout, Duration.seconds(1)));
    }

    @FXML
    public void Signin(ActionEvent actionEvent) {
        ((Hyperlink) actionEvent.getSource()).setVisited(false);
        PageHandler.getInstance().activePage("sign_in");
    }
}
