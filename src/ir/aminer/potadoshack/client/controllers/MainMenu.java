package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.views.*;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class MainMenu extends Page {
    @FXML
    private ImageView img_user_pfp;
    @FXML
    private Label lbl_user_name;
    @FXML
    private BorderPane border_pane;

    @FXML
    private Button btn_meals;
    @FXML
    private Button btn_cart;
    @FXML
    private Button btn_orders;
    @FXML
    private Button btn_settings;

    private Button selectedButton = btn_meals;

    public MainMenu() {
        super("layouts/MainMenu.fxml");
    }

    @Override
    public void onEnter(Page from) {
        lbl_user_name.setText(User.loadClient().getUsername());
    }

    @FXML
    public void initialize() throws IOException {
        /* make profile picture circle */
        img_user_pfp.setClip(new Circle(img_user_pfp.getFitWidth() / 2, img_user_pfp.getFitHeight() / 2, img_user_pfp.getFitWidth() / 2));

        FXMLLoader loader = new FXMLLoader(PotadoShack.class.getResource("layouts/views/ViewMeals.fxml"));
        loader.setController(new ViewMeals(this));
        border_pane.setCenter(loader.load());
        selectedButton = btn_meals;
    }

    public Node getCenter() {
        return border_pane.getCenter();
    }

    public void selectView(View view) {
        final View.Type type = view.getType();

        selectedButton.getStyleClass().remove("selected");
        selectedButton.getGraphic().getStyleClass().remove("selected-icon");

        FXMLLoader loader;
        if (type.equals(View.Type.MEALS)) {
            loader = new FXMLLoader(PotadoShack.class.getResource("layouts/views/ViewMeals.fxml"));

            selectedButton = btn_meals;
        } else if (type.equals(View.Type.CART)) {
            loader = new FXMLLoader(PotadoShack.class.getResource("layouts/views/ViewCart.fxml"));

            selectedButton = btn_cart;
        } else if (type.equals(View.Type.ORDERS)) {
            loader = new FXMLLoader(PotadoShack.class.getResource("layouts/views/ViewOrder.fxml"));

            selectedButton = btn_orders;
        } else if (type.equals(View.Type.SETTINGS)) {
            loader = new FXMLLoader(PotadoShack.class.getResource("layouts/views/ViewSettings.fxml"));
            selectedButton = btn_settings;
        } else {
            throw new IllegalArgumentException("given type is not valid");
        }

        selectedButton.getStyleClass().add("selected");
        selectedButton.getGraphic().getStyleClass().add("selected-icon");

        loader.setController(view);

        try {
            border_pane.setCenter(loader.load());
        } catch (IOException ignored) {
            System.err.println("Couldn't load fxml file");
        }

    }

    @FXML
    private void onMeals() {
        selectView(new ViewMeals(this));
    }

    @FXML
    private void onViewCart() {
        selectView(new ViewCart(this, User.loadClient().getOrder()));
    }

    @FXML
    private void onViewOrders() {
        selectView(new ViewOrder(this));
    }

    @FXML
    private void onSettings() {
        selectView(new ViewSettings(this));
    }

    @FXML
    private void onLogout() {
        if (User.getPreferenceFile().delete())
            PageHandler.getInstance().activePage("sign_in");
    }
}
