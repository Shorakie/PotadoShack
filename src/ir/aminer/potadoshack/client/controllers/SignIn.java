package ir.aminer.potadoshack.client.controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.network.packets.SignInPacket;
import ir.aminer.potadoshack.core.network.packets.UserPacket;
import ir.aminer.potadoshack.core.utils.AnimationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SignIn extends Page {
    public AnchorPane body;
    public TextField txt_username;
    public PasswordField txt_password;

    private JFXSnackbar snackbar;

    public SignIn() {
        super("layouts/SignIn.fxml");
    }

    @FXML
    public void initialize() {
        snackbar = new JFXSnackbar(body);
    }

    @Override
    public void onEnter(Page from) {
        if (User.hasPreference() && User.loadClient().getJwt() != null)
            PageHandler.getInstance().activePage("main_menu");
    }

    public void signin(ActionEvent actionEvent) throws IOException {
        ClientSocket client = new ClientSocket(Main.host, Main.port);

        SignInPacket signin = new SignInPacket(txt_username.getText(), txt_password.getText());
        client.sendPacket(signin);

        handleResponse(client);
    }

    @Override
    public void onResponse(ResponsePacket response) {
        UserPacket packet;
        if (response.getResponse() instanceof UserPacket)
            packet = (UserPacket) response.getResponse();
        else
            return;

        User user;
        if (User.hasPreference())
            user = User.loadClient();
        else {
            user = new User(packet.getUser());
            try {
                User.getPreferenceFile().createNewFile();
            } catch (IOException ioException) {
                System.err.println("Couldn't create preference file");
            }
        }
        user.setJwt(packet.getJwt());
        user.save();

        PageHandler.getInstance().activePage("main_menu");
    }

    @Override
    public void onError(Error error) {
        JFXSnackbarLayout layout = new JFXSnackbarLayout("An error occurred!");
        layout.getStyleClass().add("error");

        if (error.equals(Error.WRONG_PASSWORD)) {
            AnimationUtils.pulse(txt_password).play();
            layout.setToast("The password is incorrect.");
        } else if (error.equals(Error.USERNAME_NOT_FOUND)) {
            AnimationUtils.pulse(txt_username).play();
            layout.setToast("No user with the username exists.");
        }

        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(layout));
    }

    public void Signup(ActionEvent actionEvent) {
        ((Hyperlink) actionEvent.getSource()).setVisited(false);
        PageHandler.getInstance().activePage("sign_up");
    }
}
