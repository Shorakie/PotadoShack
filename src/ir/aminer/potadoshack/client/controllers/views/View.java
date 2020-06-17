package ir.aminer.potadoshack.client.controllers.views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.core.error.Error;

public abstract class View {

    public static enum Type {
        MEALS, CART, ORDERS, SETTINGS;
    }

    protected final MainMenu mainMenu;
    protected JFXSnackbar snackbar;

    public View(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.snackbar = new JFXSnackbar();
    }

    protected void errorHandler(Error error) {
        JFXSnackbarLayout layout = new JFXSnackbarLayout(error.getMessage());
        layout.getStyleClass().add("error");
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(layout));
    }

    public abstract Type getType();
}
