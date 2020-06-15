package ir.aminer.potadoshack.client.controllers.views;

import com.jfoenix.controls.*;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.client.controllers.custom.AddressBar;
import ir.aminer.potadoshack.core.order.Address;
import ir.aminer.potadoshack.core.utils.AnimationUtils;
import ir.aminer.potadoshack.core.utils.ExecutorUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

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
    private TextField txt_phone_code;
    @FXML
    private TextField txt_phone_number;
    @FXML
    private TextField txt_phone_number_code;


    @FXML
    private VBox address_list;

    private final static ExecutorService executorService =
            ExecutorUtils.createFixedTimeoutExecutorService(1, 1, TimeUnit.SECONDS);

    private JFXSnackbar snackbar;
    private JFXDialog dialog;

    public ViewSettings(MainMenu mainMenu) {
        super(mainMenu);
    }

    @FXML
    public void initialize() {
        /* Initialize SnackBar */
        snackbar = new JFXSnackbar(body);
        /* Initialize JFXDialog */
        dialog = new JFXDialog();
        dialog.setDialogContainer(body);

        /// Profile Section
        /* make profile picture circle */
        double radius = profile_img_frame.getPrefWidth() / 2;
        profile_img_frame.setClip(new Circle(radius, radius, radius));

        /* Add fade in/out animation for change pfp button*/
        profile_img_btn.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> AnimationUtils.fade(profile_img_btn, false).play());
        profile_img_btn.addEventHandler(MouseEvent.MOUSE_EXITED,  event -> AnimationUtils.fade(profile_img_btn, true).play());


        /* Fill labels */
        User user = User.loadClient();
        lbl_username.setText(user.getUsername());
        txt_first_name.setText(user.getFirstName());
        txt_last_name.setText(user.getLastName());
        txt_email.setText(user.getEmail());

        /// Password Section

        /// Addresses Section
        executorService.submit(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (Address address : User.loadClient().getAddresses()){
                    if (isCancelled())
                        break;

                    AddressBar addressBar = new AddressBar(dialog);
                    addressBar.setAddress(address);
                    Platform.runLater(()->address_list.getChildren().add(addressBar));
                }
                return null;
            }
        });


    }

    @FXML
    public void changeProfilePic(){
    }

    @FXML
    public void onNewAddress() throws IOException {
        GridPane gridPane = FXMLLoader.load(getClass().getResource("../custom/AddressDialog.fxml"));
        Button save_btn = ((Button)gridPane.lookup("#save"));
        TextField add_name = ((TextField)gridPane.lookup("#name"));
        TextArea add_location = ((TextArea)gridPane.lookup("#address"));
        save_btn.setOnAction(event ->{
            User user = User.loadClient();
            Address address = new Address(add_name.getText(), add_location.getText());
            user.getAddresses().add(address);
            user.save();

            AddressBar addressBar = new AddressBar(dialog);
            addressBar.setAddress(address);
            address_list.getChildren().add(addressBar);

            dialog.close();
        });

        dialog.setContent(gridPane);
        dialog.show();
    }

    @Override
    public Type getType() {
        return Type.SETTINGS;
    }
}
