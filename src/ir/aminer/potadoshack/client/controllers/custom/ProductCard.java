package ir.aminer.potadoshack.client.controllers.custom;

import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.custom.event.ProductChangeEvent;
import ir.aminer.potadoshack.core.product.Product;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Following {} Document.
 */
public class ProductCard extends AnchorPane {

    public Pane head;
    public ImageView thumbnail;
    public Label txt_name;
    public TextField txt_count;
    public Label lbl_price;

    private String color = "black";
    private Product product;

    private Consumer<ProductChangeEvent> orderEventConsumer = orderEvent -> {};

    public ProductCard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductCard.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @FXML
    public void initialize(){
        txt_count.setTextFormatter(new TextFormatter<Object>(c -> {
            if(c.getControlNewText().isEmpty())
                return c;
            int n;
            try{
                n = Integer.parseInt(c.getControlNewText());
            }catch (NumberFormatException exception){
                return null;
            }

            if (n > 99 || n < 1)
                return null;

            return c;
        }));
    }

    /// Product property
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;

        setThumbnail(new Image(product.getImageUrl()));
        setName(product.getName());
        setColor(product.getCategory().getColor());
        setPrice(String.valueOf(product.getPrice()));
    }

    /// color property
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        head.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 5 5 0 0;", color));
    }

    /// thumbnail property
    public Image getThumbnail() {
        return thumbnailProperty().get();
    }

    public void setThumbnail(Image thumbnail) {
        thumbnailProperty().set(thumbnail);
    }

    public ObjectProperty<Image> thumbnailProperty() {
        return thumbnail.imageProperty();
    }

    /// name property
    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public StringProperty nameProperty() {
        return txt_name.textProperty();
    }

    /// price property
    public String getPrice() {
        return priceProperty().get();
    }

    public void setPrice(String price) {
        priceProperty().set(price);
    }

    public StringProperty priceProperty() {
        return lbl_price.textProperty();
    }
    
    /// On Order event
    public Consumer<ProductChangeEvent> getOnOrder() {
        return orderEventConsumer;
    }

    public void setOnOrder(Consumer<ProductChangeEvent> eventHandler) {
        orderEventConsumer = eventHandler;
    }

    /// public methods
    public int getCount() {
        return Integer.parseInt(txt_count.getText());
    }

    @FXML
    private void onOrder() {
        orderEventConsumer.accept(new ProductChangeEvent(getProduct(), getCount()));
    }
}
