package ir.aminer.potadoshack.client.page;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.callbacks.IPageEnter;
import ir.aminer.potadoshack.client.page.callbacks.IPageError;
import ir.aminer.potadoshack.client.page.callbacks.IPageExit;
import ir.aminer.potadoshack.client.page.callbacks.IPageResponse;

import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.ErrorPacket;
import ir.aminer.potadoshack.core.network.packets.ResponsePacket;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.utils.Common;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.EOFException;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Page implements IPageEnter, IPageExit, IPageResponse, IPageError {
    private final Parent parent;

    // TODO: Rework constructors

    public Page(String path) {
        Parent temp_parent;
        try {
            FXMLLoader loader = new FXMLLoader(PotadoShack.class.getResource(path));
            loader.setController(this);
            temp_parent = loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.err.println("Could not load: " + path);
            temp_parent = null;
        }
        this.parent = temp_parent;
    }

    public Page(Parent parent) {
        this.parent = parent;
    }

    public Parent getParent() {
        return parent;
    }

    public void handleResponse(ClientSocket client) throws IOException {
        client.handleResponseAfterAuthority(this::onResponse, this::onError);
    }

    public void onResponse(ResponsePacket response) {
    }

    public void onError(Error error) {
    }

    public void onEnter(Page from) {
    }

    public void onExit(Page to) {
    }
}
