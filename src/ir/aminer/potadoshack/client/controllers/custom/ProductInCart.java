package ir.aminer.potadoshack.client.controllers.custom;

import ir.aminer.potadoshack.client.controllers.custom.event.ProductChangeEvent;
import ir.aminer.potadoshack.core.product.Product;
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
public class ProductInCart extends AnchorPane {

    @FXML
    private Label lbl_icon;
    @FXML
    private Label lbl_count;
    @FXML
    private Label lbl_name;
    @FXML
    private Label lbl_price;
    @FXML
    private Label lbl_total_price;

    @FXML
    private Button btn_minus;
    @FXML
    private Button btn_plus;
    @FXML
    private Button btn_delete;

    private Product product;
    boolean readOnly = false;

    private Consumer<ProductChangeEvent> deleteEventConsumer = deleteEvent -> {};
    private final Consumer<ProductChangeEvent> deleteFromView = deleteEvent -> {
        try {
            ((Pane) this.getParent()).getChildren().remove(this);
        } catch (Exception ignored) {
            System.err.println("Parent of the Card is not a Pane");
        }
    };

    private Consumer<ProductChangeEvent> countChangeEvent = countEvent -> {
    };

    public ProductInCart() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductInCart.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /// Product property
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        setIcon(product.getCategory().getIcon());
        setIconColor(product.getCategory().getColor());
        setName(product.getName());
        setPrice(product.getPrice());
    }

    public boolean getReadOnly(){
        return readOnly;
    }

    public void setReadOnly(boolean readOnly){
        this.readOnly = readOnly;

        btn_plus.setDisable(readOnly);
        btn_minus.setDisable(readOnly);
        btn_delete.setDisable(readOnly);
    }

    /// icon property
    // icon content
    public String getIcon() {
        return iconProperty().get();
    }

    public void setIcon(String icon) {
        iconProperty().set(icon);
    }

    public StringProperty iconProperty() {
        return lbl_icon.textProperty();
    }

    // icon color
    public void setIconColor(String color){
        lbl_icon.setStyle(String.format("-icon-color: %s;", color));
    }

    /// count property
    public Integer getCount() {
        return Integer.valueOf(countProperty().get());
    }

    public void setCount(Integer count) {
        if (count < 1 || count > 99)
            return;
        countProperty().set(count.toString());
        updateTotalPrice();
    }

    public StringProperty countProperty() {
        return lbl_count.textProperty();
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

    /// price property
    public Integer getPrice() {
        return Integer.valueOf(priceProperty().get());
    }

    public Integer getTotalPrice() {
        return Integer.valueOf(lbl_total_price.textProperty().get());
    }

    public void setPrice(Integer price) {
        priceProperty().set(price.toString());
        updateTotalPrice();
    }

    public StringProperty priceProperty() {
        return lbl_price.textProperty();
    }

    /// Events Properties
    // On Delete
    public Consumer<ProductChangeEvent> getOnDelete() {
        return deleteEventConsumer;
    }

    public void setOnDelete(Consumer<ProductChangeEvent> eventHandler) {
        deleteEventConsumer = eventHandler;
    }

    // On Count change
    public Consumer<ProductChangeEvent> getOnCountChange() {
        return countChangeEvent;
    }

    public void setOnCountChange(Consumer<ProductChangeEvent> eventHandler) {
        this.countChangeEvent = eventHandler;
    }

    // Private Methods
    private void updateTotalPrice() {
        lbl_total_price.textProperty().set(Integer.toString(getPrice() * getCount()));
    }

    @FXML
    private void onMinus() {
        setCount(getCount() - 1);
        countChangeEvent.accept(new ProductChangeEvent(getProduct(), getCount()));
    }

    @FXML
    private void onPlus() {
        setCount(getCount() + 1);
        countChangeEvent.accept(new ProductChangeEvent(getProduct(), getCount()));
    }

    @FXML
    private void onDelete() {
        deleteEventConsumer
                .andThen(deleteFromView)
                .accept(new ProductChangeEvent(getProduct(), getCount()));
    }
}