package ir.aminer.potadoshack.client.controllers.views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.client.controllers.custom.ProductInCart;
import ir.aminer.potadoshack.client.controllers.custom.event.ProductChangeEvent;
import ir.aminer.potadoshack.core.error.Error;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.PlaceOrderPacket;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.order.Address;
import ir.aminer.potadoshack.core.order.Order;
import ir.aminer.potadoshack.core.product.Product;
import ir.aminer.potadoshack.core.utils.ExecutorUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ViewCart extends View {

    @FXML
    private VBox v_box;
    @FXML
    private Label lbl_total_price;
    @FXML
    private Button btn_submit;
    @FXML
    private JFXComboBox<Address> address_list;

    private boolean readOnly;

    ExecutorService executorService = ExecutorUtils.createFixedTimeoutExecutorService(2, 1, TimeUnit.SECONDS);
    Supplier<Task<Void>> updateGrandTotalPriceFactory = () -> new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            int grand_total_price = 0;

            /* Add products in cart */
            for (HashMap.Entry<Product, Integer> product : order.getCart().getProducts())
                grand_total_price += product.getKey().getPrice() * product.getValue();

            int finalGrand_total_price = grand_total_price;
            Platform.runLater(() -> lbl_total_price.setText(Integer.toString(finalGrand_total_price)));
            return null;
        }
    };

    private final Order order;

    public ViewCart(MainMenu mainMenu, Order order) {
        super(mainMenu);
        this.order = order;
        setReadOnly(isReadOnly(order));
    }

    @FXML
    public void initialize() {
        v_box.getChildren().clear();

        executorService.submit(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                /* Add products in cart */
                for (HashMap.Entry<Product, Integer> product : ViewCart.this.order.getCart().getProducts()) {
                    if (isCancelled())
                        break;

                    ProductInCart productInfo = new ProductInCart();
                    productInfo.setReadOnly(isReadOnly());
                    productInfo.setProduct(product.getKey());
                    productInfo.setCount(product.getValue());

                    productInfo.setOnDelete(ViewCart.this::onDelete);
                    productInfo.setOnCountChange(ViewCart.this::onCountChange);

                    Platform.runLater(() -> v_box.getChildren().add(productInfo));
                }

                return null;
            }
        });

        List<Address> addresses = User.loadClient().getAddresses();
        for (Address address : addresses)
            address_list.getItems().add(address);

        if (order.getAddress() != null) {
            if (!address_list.getItems().contains(order.getAddress()))
                address_list.getItems().add(order.getAddress());

            address_list.setValue(order.getAddress());
        }

        executorService.submit(updateGrandTotalPriceFactory.get());
    }

    private boolean isReadOnly(Order order) {
        return !order.getStatus().equals(Order.Status.CREATED);
    }

    public final boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;

        executorService.submit(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    btn_submit.setDisable(readOnly);
                    btn_submit.setVisible(!readOnly);
                });

                for (Node node : v_box.getChildren()) {
                    if (!(node instanceof ProductInCart))
                        continue;

                    Platform.runLater(() -> ((ProductInCart) node).setReadOnly(readOnly));
                }
                return null;
            }
        });
    }

    public void onDelete(ProductChangeEvent event) {
        System.out.println("Removing " + event.getProduct().getName() + " From cart");

        User user = User.loadClient();
        user.getOrder().getCart().removeProduct(event.getProduct());
        System.out.println(user.getOrder().getCart());
        user.save();

        /* Update Grand Total price */
        executorService.submit(updateGrandTotalPriceFactory.get());
    }

    public void onCountChange(ProductChangeEvent event) {
        System.out.println("Setting " + event.getAmount() + "*" + event.getProduct().getName());

        User user = User.loadClient();
        user.getOrder().getCart().setProduct(event.getProduct(), event.getAmount());
        System.out.println(user.getOrder().getCart());
        user.save();

        /* Update Grand Total price */
        executorService.submit(updateGrandTotalPriceFactory.get());
    }

    @FXML
    public void onSubmit(ActionEvent event) throws IOException {
        if (address_list.getValue() == null) {
            (new JFXSnackbar(v_box)).enqueue(new JFXSnackbar.SnackbarEvent(new Label("Amin :D"), new Duration(5000)));
            return;
        }

        ClientSocket client = new ClientSocket(Main.host, Main.port);
        User user = User.loadClient();
        user.getOrder().setAddress(address_list.getValue());
        user.getOrder().close();

        PlaceOrderPacket packet = new PlaceOrderPacket(
                user.getJwt().toString(),
                user.getOrder());

        client.sendPacket(packet);

        client.handleResponseAfterAuthority(responsePacket -> {
            setReadOnly(true);

            user.renewCart();
            user.save();

        }, System.err::println);

        client.close();
    }

    @Override
    public Type getType() {
        return Type.CART;
    }
}