package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.custom.OrderCard;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.CancelOrderPacket;
import ir.aminer.potadoshack.core.network.packets.PrimitivePacket;
import ir.aminer.potadoshack.core.network.packets.ViewOrdersPacket;
import ir.aminer.potadoshack.core.order.Order;
import ir.aminer.potadoshack.core.utils.ExecutorUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewOrder {
    @FXML private VBox v_box;

    private MainMenu mainMenu = null;

    ExecutorService executorService =
            ExecutorUtils.createFixedTimeoutExecutorService(1, 1, TimeUnit.SECONDS);

    @FXML
    public void initialize() {
        Task<Void> addOrders = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                ClientSocket client = new ClientSocket(Main.host, Main.port);

                client.sendPacket(new ViewOrdersPacket(User.loadClient().getJwt().toString()));

                client.handleResponse(responsePacket -> {
                    /* Add products in cart */
                    for (HashMap.Entry<Integer, Order> order : ((ViewOrdersPacket) responsePacket.getResponse()).getOrders().entrySet()) {
                        OrderCard orderCard = new OrderCard();
                        orderCard.setOrder(order.getValue());

                        orderCard.setOnDelete(ViewOrder.this::onDelete);
                        orderCard.setOnView(ViewOrder.this::onView);

                        Platform.runLater(() -> v_box.getChildren().add(orderCard));
                    }
                }, System.err::println);

                client.close();

                return null;
            }
        };
        executorService.submit(addOrders);
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public void onDelete(Order order) {
        try {
            ClientSocket client = new ClientSocket(Main.host, Main.port);

            client.sendPacket(new CancelOrderPacket(User.loadClient().getJwt().toString(), order.getCode()));

            client.handleResponse(responsePacket -> {
                System.out.println(((PrimitivePacket<String>)responsePacket.getResponse()).getData());
            }, System.err::println);


            client.close();
        }catch (IOException ioException){
            System.err.println("Couldn't send order delete packet");
        }
    }

    public void onView(Order order) {
        ViewCart viewCart = (ViewCart) mainMenu.selectView(MainMenu.View.CART);
        viewCart.setOrder(order);
    }

}
