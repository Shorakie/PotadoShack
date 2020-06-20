package ir.aminer.potadoshack.client.controllers.views;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.client.controllers.custom.AddressBar;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.UpdatePasswordPacket;
import ir.aminer.potadoshack.core.network.packets.UpdateProfilePacket;
import ir.aminer.potadoshack.core.order.Address;
import ir.aminer.potadoshack.core.user.PhoneNumber;
import ir.aminer.potadoshack.core.utils.AnimationUtils;
import ir.aminer.potadoshack.core.utils.Common;
import ir.aminer.potadoshack.core.utils.Validators;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewSettings extends View {
    @FXML
    private StackPane body;

    /// Profile nodes
    @FXML
    private AnchorPane profile_img_frame;
    @FXML
    private Button profile_img_btn;
    @FXML
    private Label lbl_username;
    @FXML
    private TextField txt_first_name;
    @FXML
    private TextField txt_last_name;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_phone_number;
    @FXML
    private TextField txt_phone_number_code;
    @FXML
    private ImageView img_profile_picture;

    /// Passowrd Nodes
    @FXML
    private TextField txt_old_password;
    @FXML
    private TextField txt_new_password;
    @FXML
    private TextField txt_new_password_confirm;

    /// Addresses nodes
    @FXML
    private VBox address_list;

    private final static ExecutorService executorService =
            Common.createFixedTimeoutExecutorService(1, 1, TimeUnit.SECONDS);

    private JFXDialog dialog;

    public ViewSettings(MainMenu mainMenu) {
        super(mainMenu);
    }

    @FXML
    public void initialize() {
        /* Initialize SnackBar */
        snackbar.registerSnackbarContainer(body);
        /* Initialize JFXDialog */
        dialog = new JFXDialog();
        dialog.setDialogContainer(body);

        /// Profile Section
        /* make profile picture circle */
        double radius = profile_img_frame.getPrefWidth() / 2;
        profile_img_frame.setClip(new Circle(radius, radius, radius));

        /* Add fade in/out animation for change pfp button*/
        profile_img_btn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> AnimationUtils.fade(profile_img_btn, false).play());
        profile_img_btn.addEventHandler(MouseEvent.MOUSE_EXITED, event -> AnimationUtils.fade(profile_img_btn, true).play());

        /* Text Formatter */
        txt_phone_number_code.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty())
                return change;

            if (!change.getControlNewText().startsWith("+"))
                return null;

            int n;
            try {
                if (!change.getControlNewText().substring(1).isEmpty())
                    n = Integer.parseInt(change.getControlNewText().substring(1));
                else
                    n = 1;
            } catch (NumberFormatException ignored) {
                return null;
            }

            if (n < 1 || n > 999)
                return null;

            return change;
        }));
        txt_phone_number.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty())
                return change;

            if (!change.getControlNewText().matches("\\d*"))
                return null;

            if (change.getControlNewText().length() > 10)
                return null;

            return change;
        }));

        /* Fill labels */
        User user = User.loadClient();
        lbl_username.setText(user.getUsername());
        txt_first_name.setText(user.getFirstName());
        txt_last_name.setText(user.getLastName());
        txt_email.setText(user.getEmail());
        if (user.getPhoneNumber() != null) {
            txt_phone_number.setText(String.valueOf(user.getPhoneNumber().getNumber()));
            txt_phone_number_code.setText("+" + user.getPhoneNumber().getCode());
        }
        try {
            img_profile_picture.setImage(SwingFXUtils.toFXImage(User.loadClient().getProfilePicture(), null));
        } catch (IllegalStateException ignored) {
        }


        /// Password Section

        /// Addresses Section
        executorService.submit(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Address address : User.loadClient().getAddresses()) {
                    if (isCancelled())
                        break;

                    AddressBar addressBar = new AddressBar(dialog);
                    addressBar.setAddress(address);
                    addressBar.setOnDelete(ViewSettings.this::onAddressDelete);
                    Platform.runLater(() -> address_list.getChildren().add(addressBar));
                }
                return null;
            }
        });


    }

    @FXML
    public void onChangeProfilePic() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showOpenDialog(Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null));
        if (file != null)
            img_profile_picture.setImage(SwingFXUtils.toFXImage(ImageIO.read(file), null));
    }

    @FXML
    public void onSaveProfileInfo() {
        try {
            ClientSocket client = new ClientSocket(Main.host, Main.port);

            /* Validation */
            if (!txt_phone_number_code.getText().isEmpty() && txt_phone_number.getText().isEmpty()) {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Please enter a phone number!")));
                AnimationUtils.pulse(txt_phone_number).play();
                return;
            } else if (!txt_phone_number_code.getText().isEmpty() && txt_phone_number.getText().isEmpty()) {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Please enter a country code!")));
                AnimationUtils.pulse(txt_phone_number_code).play();
                return;
            } else if (!txt_phone_number.getText().isEmpty() && txt_phone_number.getText().length() != 10) {
                System.out.println(txt_phone_number.getText() + " " + txt_phone_number.getText().length());
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Please enter a valid phone number!")));
                AnimationUtils.pulse(txt_phone_number).play();
                return;
            } else if (!txt_phone_number_code.getText().isEmpty() && txt_phone_number_code.getText().length() < 2) {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Please enter a valid country code!")));
                AnimationUtils.pulse(txt_phone_number_code).play();
                return;
            }

            // URL https://emailregex.com
            if (txt_email.getText() != null && !txt_email.getText().isEmpty() && !txt_email.getText().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Please enter a valid email address!")));
                AnimationUtils.pulse(txt_email).play();
                return;
            }


            User user = User.loadClient();
            user.setFirstName(txt_first_name.getText());
            user.setLastName(txt_last_name.getText());
            user.setEmail(txt_email.getText());
            if (!txt_phone_number_code.getText().isEmpty() && !txt_phone_number.getText().isEmpty())
                user.setPhoneNumber(new PhoneNumber(
                        Integer.parseInt(txt_phone_number_code.getText().substring(1)),
                        Long.parseLong(txt_phone_number.getText())));
            user.setProfilePicture(SwingFXUtils.fromFXImage(img_profile_picture.getImage(), null));

            client.sendPacket(new UpdateProfilePacket(user.getJwt().toString(), user));

            client.handleResponseAfterAuthority(responsePacket -> {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Profile changed!")));
                user.save();
                mainMenu.updateFields();
            }, this::errorHandler);

            client.close();
        } catch (IOException ioException) {
            System.err.println("Couldn't send order delete packet");
        }
    }

    @FXML
    public void onChangePassword() {
        try {
            ClientSocket client = new ClientSocket(Main.host, Main.port);
            User user = User.loadClient();

            // Validation
            if (Validators.passwordFieldValidator(txt_new_password.getText(), error -> {
                AnimationUtils.pulse(txt_new_password).play();
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout(error)));
            }))
                return;
            else if (!txt_new_password.getText().equals(txt_new_password_confirm.getText())) {
                AnimationUtils.pulse(txt_new_password_confirm).play();
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Passwords doesn't match.")));
                return;
            }

            client.sendPacket(new UpdatePasswordPacket(user.getJwt().toString(), txt_old_password.getText(), txt_new_password.getText()));

            client.handleResponseAfterAuthority(responsePacket -> {
                snackbar.enqueue(new JFXSnackbar.SnackbarEvent(new JFXSnackbarLayout("Password changed! Please login again")));

                User.getPreferenceFile().delete();
                if (!User.getPreferenceFile().exists())
                    PageHandler.getInstance().activePage("sign_in");
            }, this::errorHandler);

            client.close();
        } catch (IOException ioException) {
            System.err.println("Couldn't send order delete packet");
        }
    }

    @FXML
    public void onNewAddress() throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("../custom/AddressDialog.fxml"));
        Button save_btn = ((Button) gridPane.lookup("#save"));
        TextField add_name = ((TextField) gridPane.lookup("#name"));
        TextArea add_location = ((TextArea) gridPane.lookup("#address"));
        save_btn.setOnAction(event -> {
            if (add_name.getText().isEmpty()) {
                AnimationUtils.pulse(add_name).play();
                return;
            } else if (add_location.getText().isEmpty()) {
                AnimationUtils.pulse(add_location).play();
                return;
            }

            User user = User.loadClient();
            Address address = new Address(add_name.getText(), add_location.getText());
            user.getAddresses().add(address);
            user.save();

            AddressBar addressBar = new AddressBar(dialog);
            addressBar.setAddress(address);
            addressBar.setOnDelete(this::onAddressDelete);
            address_list.getChildren().add(addressBar);

            dialog.close();
        });

        dialog.setContent(gridPane);
        dialog.show();
    }

    private void onAddressDelete(Address address) {
        User user = User.loadClient();
        user.getAddresses().remove(address);
        for (Address ad : user.getAddresses())
            System.out.println(ad.toString());
        user.save();
    }

    @Override
    public Type getType() {
        return Type.SETTINGS;
    }
}
