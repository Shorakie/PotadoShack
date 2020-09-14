package ir.aminer.potadoshack.client.controllers.custom;

import com.jfoenix.controls.JFXDialog;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.core.order.Address;
import ir.aminer.potadoshack.core.utils.AnimationUtils;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Following {} Document.
 */
public class AddressBar extends GridPane {

    @FXML
    private Label lbl_name;
    @FXML
    private Label lbl_address;

    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_delete;

    private final JFXDialog dialog;

    private Consumer<Address> deleteEventConsumer = deleteEvent -> {
    };
    private Consumer<Address> editEventConsumer = countEvent -> {
    };
    private final Consumer<Address> deleteFromView = deleteEvent -> {
        try {
            ((Pane) this.getParent()).getChildren().remove(this);
        } catch (Exception ignored) {
            System.err.println("Parent of the Card is not a Pane");
        }
    };

    private Address address;

    public AddressBar(JFXDialog dialog) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("./controllers/AddressBar.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        this.dialog = dialog;
    }

    /// Address property
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        setName(address.getName());
        setLocation(address.getAddress());
    }

    /// name property
    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public StringProperty nameProperty() {
        return lbl_name.textProperty();
    }

    /// Address property
    public String getLocation() {
        return locationProperty().get();
    }

    public void setLocation(String location) {
        locationProperty().set(location);
    }

    public StringProperty locationProperty() {
        return lbl_address.textProperty();
    }

    /// Events Properties
    // On Delete
    public Consumer<Address> getOnDelete() {
        return deleteEventConsumer;
    }

    public void setOnDelete(Consumer<Address> eventHandler) {
        deleteEventConsumer = eventHandler;
    }

    // On Edit change
    public Consumer<Address> getOnEdit() {
        return editEventConsumer;
    }

    public void setOnEdit(Consumer<Address> eventHandler) {
        this.editEventConsumer = eventHandler;
    }

    @FXML
    private void onEdit() throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getClassLoader().getResource("./controllers/AddressDialog.fxml"));
        Button save_btn = ((Button) gridPane.lookup("#save"));

        TextField add_name = ((TextField) gridPane.lookup("#name"));
        TextArea add_location = ((TextArea) gridPane.lookup("#address"));
        if (getAddress() != null) {
            add_name.setText(getAddress().getName());
            add_location.setText(getAddress().getAddress());
        }

        ((Label) gridPane.lookup("#head")).setText("Edit Address");

        save_btn.setOnAction(event -> {
            if (add_name.getText().isEmpty()) {
                AnimationUtils.pulse(add_name).play();
                return;
            } else if (add_location.getText().isEmpty()) {
                AnimationUtils.pulse(add_location).play();
                return;
            }

            User user = User.loadClient();
            user.getAddresses().remove(getAddress());
            Address newAddress = new Address(add_name.getText(), add_location.getText());
            user.getAddresses().add(newAddress);
            user.save();

            setAddress(newAddress);

            dialog.close();
        });

        dialog.setContent(gridPane);
        dialog.show();

        getOnEdit().accept(getAddress());
    }

    @FXML
    private void onDelete() {
        getOnDelete()
                .andThen(deleteFromView)
                .accept(getAddress());
    }
}