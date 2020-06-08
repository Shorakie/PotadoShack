package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.custom.ProductInCart;
import ir.aminer.potadoshack.client.controllers.custom.event.ProductChangeEvent;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.PlaceOrderPacket;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ViewCart {

    @FXML
    private VBox v_box;
    @FXML
    private Label lbl_total_price;
    @FXML
    private Button btn_submit;

    ExecutorService executorService = ExecutorUtils.createFixedTimeoutExecutorService(2, 1, TimeUnit.SECONDS);
    Supplier<Task<Void>> updateGrandTotalPriceFactory = () -> new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            int grand_total_price = 0;

            /* Add products in cart */
            for (HashMap.Entry<Product, Integer> product : currentOrder.getCart().getProducts())
                grand_total_price += product.getKey().getPrice() * product.getValue();

            int finalGrand_total_price = grand_total_price;
            Platform.runLater(() -> lbl_total_price.setText(Integer.toString(finalGrand_total_price)));
            return null;
        }
    };

    private Order currentOrder;

    @FXML
    public void initialize() {
        setOrder(User.loadClient().getOrder());
        executorService.submit(updateGrandTotalPriceFactory.get());
    }

    public void setOrder(Order order) {
        v_box.getChildren().clear();
        this.currentOrder = order;

        final boolean isReadOnly = isReadOnly();

        Task<Void> addProductsInCart = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                btn_submit.setDisable(isReadOnly);
                btn_submit.setVisible(!isReadOnly);

                /* Add products in cart */
                for (HashMap.Entry<Product, Integer> product : currentOrder.getCart().getProducts()) {
                    System.out.println(product.getKey());
                    ProductInCart productInfo = new ProductInCart();
                    productInfo.setReadOnly(isReadOnly);
                    productInfo.setProduct(product.getKey());
                    productInfo.setCount(product.getValue());

                    productInfo.setOnDelete(ViewCart.this::onDelete);
                    productInfo.setOnCountChange(ViewCart.this::onCountChange);

                    Platform.runLater(() -> v_box.getChildren().add(productInfo));
                }

                return null;
            }
        };
        try {
            executorService.submit(addProductsInCart).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private boolean isReadOnly() {
        return !currentOrder.getStatus().equals(Order.Status.CREATED);
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
        ClientSocket client = new ClientSocket(Main.host, Main.port);
        User user = User.loadClient();
        user.getOrder().close();

        PlaceOrderPacket packet = new PlaceOrderPacket(
                user.getJwt().toString(),
                user.getOrder());

        client.sendPacket(packet);

        client.handleResponse(responsePacket -> {
            System.out.println(((PrimitivePacket<Integer>)responsePacket.getResponse()).getData());
            setOrder(user.getOrder());
            user.renewCart();
            user.save();

        }, System.err::println);

        client.close();
    }
}