package ir.aminer.potadoshack.client.controllers;

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
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;

public class SignUp extends Page {
    public TextField txt_username;
    public PasswordField txt_password;
    public PasswordField txt_password_confirm;
    public TextField txt_first_name;
    public TextField txt_last_name;
    public Button btn_signup;
    public ImageView img;
    public Label error_span;

    public SignUp() {
        super("layouts/SignUp.fxml");
    }

    public void signup(ActionEvent actionEvent) throws IOException, InterruptedException, ClassNotFoundException {
        if (!txt_password.getText().equals(txt_password_confirm.getText())) {
            // TODO: Show error span
            txt_password_confirm.setText("");
        }

        // TODO: Validate other fields

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

            // TODO: ERROR SNACK
            error_span.setVisible(true);
            TranslateTransition tt = new TranslateTransition(Duration.millis(1000));
            tt.setFromY(error_span.getHeight());
            tt.setToY(0);
            FadeTransition ft = new FadeTransition(Duration.millis(250));
            ft.setByValue(-100);
            ft.setDelay(Duration.millis(3000));
            SequentialTransition st = new SequentialTransition(error_span, tt, ft);
            st.play();
        }
        System.out.println("Got an error: " + error); // TODO: Handle events in error snack
    }

    public void Signin(ActionEvent actionEvent) {
        ((Hyperlink) actionEvent.getSource()).setVisited(false);
        PageHandler.getInstance().activePage("sign_in");
    }
}
