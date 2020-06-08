package ir.aminer.potadoshack.client.controllers.custom;

import ir.aminer.potadoshack.core.order.Order;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Following {} Document.
 */
public class OrderCard extends AnchorPane {

    @FXML
    private Label lbl_code;
    @FXML
    private Label lbl_total_price;

    @FXML
    private Button btn_view;
    @FXML
    private Button btn_delete;

    private Order order;

    private Consumer<Order> deleteEventConsumer = deleteEvent -> {};
    private Consumer<Order> viewEventConsumer = countEvent -> {};
    private final Consumer<Order> deleteFromView = deleteEvent -> {
        try {
            ((Pane) this.getParent()).getChildren().remove(this);
        } catch (Exception ignored) {
            System.err.println("Parent of the Card is not a Pane");
        }
    };


    public OrderCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrdersCard.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /// Order property
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        setCode(order.getCode());
        setTotalPrice(order.getTotalPrice());
    }

    /// code property
    public Integer getCode() {
        return Integer.valueOf(codeProperty().get());
    }

    public void setCode(Integer code) {
        codeProperty().set(code.toString());
    }

    public StringProperty codeProperty() {
        return lbl_code.textProperty();
    }

    /// price property
    public Integer getTotalPrice() {
        return Integer.valueOf(totalPriceProperty().get());
    }

    public void setTotalPrice(Integer price) {
        totalPriceProperty().set(price.toString());
    }

    public StringProperty totalPriceProperty() {
        return lbl_total_price.textProperty();
    }

    /// Events Properties
    // On Delete
    public Consumer<Order> getOnDelete() {
        return deleteEventConsumer;
    }

    public void setOnDelete(Consumer<Order> eventHandler) {
        deleteEventConsumer = eventHandler;
    }

    // On Count change
    public Consumer<Order> getOnView() {
        return viewEventConsumer;
    }

    public void setOnView(Consumer<Order> eventHandler) {
        this.viewEventConsumer = eventHandler;
    }

    @FXML
    private void onView() {
        viewEventConsumer.accept(getOrder());
    }

    @FXML
    private void onDelete() {
        deleteEventConsumer
                .andThen(deleteFromView)
                .accept(getOrder());
    }
}