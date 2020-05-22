package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.core.packets.Packet;
import ir.aminer.potadoshack.core.packets.SigninPacket;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SignUp {
    public TextField txt_username;
    public TextField txt_password;
    public TextField txt_password_confirm;
    public TextField txt_first_name;
    public TextField txt_last_name;
    public Button btn_signup;

    public void signup(ActionEvent actionEvent) throws IOException, InterruptedException {
        Socket client = new Socket("localhost", 25565);
        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        Packet signin = new SigninPacket();
        outputStream.writeObject(signin);
        outputStream.flush();
        client.close();
    }
}
