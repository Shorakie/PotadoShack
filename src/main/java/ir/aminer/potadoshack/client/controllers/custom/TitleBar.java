package ir.aminer.potadoshack.client.controllers.custom;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

public class TitleBar {

    private double offsetX = 0.0f;
    private double offsetY = 0.0f;

    public void drag(MouseEvent mouseEvent) {
        Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        offsetX = window.getX() - mouseEvent.getScreenX();
        offsetY = window.getY() - mouseEvent.getScreenY();
    }

    public void dragging(MouseEvent mouseEvent) {
        Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        window.setX(mouseEvent.getScreenX() + offsetX);
        window.setY(mouseEvent.getScreenY() + offsetY);
    }

    public void onExit(ActionEvent event) {
        Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        ((Stage) window).close();
    }

    public void onMinimize(ActionEvent event) {
        Window window = Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null);
        ((Stage) window).setIconified(true);
    }
}

