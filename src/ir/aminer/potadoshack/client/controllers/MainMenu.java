package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.client.PotadoShack;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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


    public static enum View{
        MEALS, ORDERS, CART, SETTINGS
    }

    private View selectedView;
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

        border_pane.setCenter(FXMLLoader.load(PotadoShack.class.getResource("layouts/ViewMeals.fxml")));
        selectedView = View.MEALS;
        selectedButton = btn_meals;
    }

    public Object selectView(View view) {
        if (selectedView.equals(view))
            return null;

        Object control = null;

        selectedView = view;
        selectedButton.getStyleClass().remove("selected");
        selectedButton.getGraphic().getStyleClass().remove("selected-icon");

        try {
            if (view.equals(View.MEALS)) {
                FXMLLoader loader = new FXMLLoader(PotadoShack.class.getResource("layouts/ViewMeals.fxml"));
                ScrollPane pane = loader.load();
                control = loader.getController();
                border_pane.setCenter(pane);
                selectedButton = btn_meals;

            } else if (view.equals(View.CART)) {
                FXMLLoader loader = new FXMLLoader(PotadoShack.class.getResource("layouts/ViewCart.fxml"));
                BorderPane pane = loader.load();
                control = loader.getController();
                border_pane.setCenter(pane);
                selectedButton = btn_cart;
            } else if (view.equals(View.ORDERS)) {
                FXMLLoader loader = new FXMLLoader(PotadoShack.class.getResource("layouts/ViewOrder.fxml"));
                ScrollPane pane = loader.load();
                control = loader.getController();

                ((ViewOrder) control).setMainMenu(this);

                border_pane.setCenter(pane);
                selectedButton = btn_orders;
            } else if (view.equals(View.SETTINGS)) {
                selectedButton = btn_settings;
            }
        } catch (IOException ignored) {
            System.err.println("Couldn't load fxml files");
        }
        selectedButton.getStyleClass().add("selected");
        selectedButton.getGraphic().getStyleClass().add("selected-icon");

        return control;
    }

    @FXML
    private void onMeals() throws IOException {
        selectView(View.MEALS);
    }

    @FXML
    private void onViewCart() throws IOException {
        selectView(View.CART);
    }

    @FXML
    private void onViewOrders() throws IOException {
        selectView(View.ORDERS);
    }

    @FXML
    private void onSettings() throws IOException {
        selectView(View.SETTINGS);
    }

    @FXML
    private void onLogout() {
        User.getPreferenceFile().delete();
        PageHandler.getInstance().activePage("sign_in");
    }
}
