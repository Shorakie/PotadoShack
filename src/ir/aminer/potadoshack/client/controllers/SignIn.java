package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.auth.simplejwt.JWT;
import ir.aminer.potadoshack.core.auth.simplejwt.UserPayload;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.network.packets.SignInPacket;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SignIn extends Page {
    public Button btn_signin;
    public TextField txt_username;
    public PasswordField txt_password;

    public SignIn() {
        super("layouts/SignIn.fxml");
    }

//    public SignIn(String path) {
//        super(path);
//    }

    @Override
    public void onEnter(Page from) {
        if (User.hasPreference() && User.loadClient().getJwt() != null)
            PageHandler.getInstance().activePage("main_menu");
    }

    public void signin(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        ClientSocket client = new ClientSocket(Main.host, Main.port);

        SignInPacket signin = new SignInPacket(txt_username.getText(), txt_password.getText());
        client.sendPacket(signin);

        handleResponse(client);
    }

    @Override
    public void onResponse(ResponsePacket response) {

        User user;
        if (User.hasPreference())
            user = User.loadClient();
        else {
            UserPayload payload = JWT.decode(((PrimitivePacket<String>) response.getResponse()).getData()).getPayload();
            user = new User(payload.getUsername(), payload.getFirstName(), payload.getLastName());
            try {
                User.getPreferenceFile().createNewFile();
            } catch (IOException ioException) {
                System.err.println("Couldn't create preference file");
            }
        }
        user.setJwt(((PrimitivePacket<String>) response.getResponse()).getData());
        user.save();
        PageHandler.getInstance().activePage("main_menu");
    }

    @Override
    public void onError(Error error) {
        System.err.println("Got Error: " + error);
    }

    public void Signup(ActionEvent actionEvent) {
        ((Hyperlink) actionEvent.getSource()).setVisited(false);
        PageHandler.getInstance().activePage("sign_up");
    }
}
