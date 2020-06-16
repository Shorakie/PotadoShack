package ir.aminer.potadoshack.client.controllers.views;

import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.client.controllers.custom.ProductCard;
import ir.aminer.potadoshack.client.controllers.custom.event.ProductChangeEvent;
import ir.aminer.potadoshack.core.product.Food;
import ir.aminer.potadoshack.core.utils.ExecutorUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewMeals extends View {
    @FXML
    private ScrollPane scroll_pane;
    @FXML
    private TilePane tile_pane;

    private final static ExecutorService executorService =
            ExecutorUtils.createFixedTimeoutExecutorService(1, 1, TimeUnit.SECONDS);

    public ViewMeals(MainMenu mainMenu) {
        super(mainMenu);
    }

    @FXML
    public void initialize() {
        Task<Void> addFoodsThread = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                /* Add foods */
                for (Food food : Food.values()) {
                    if (isCancelled())
                        break;

                    ProductCard productCard = new ProductCard();
                    productCard.setProduct(food);
                    productCard.setOnOrder(ViewMeals.this::onOrder);

                    Platform.runLater(() -> tile_pane.getChildren().add(productCard));
                }
                return null;
            }
        };

        executorService.submit(addFoodsThread);
    }

    public void onOrder(ProductChangeEvent event) {
        System.out.println("Adding " + event.getAmount() + "*" + event.getProduct().getName());
        User user = User.loadClient();
        user.getOrder().getCart().addProduct(event.getProduct(), event.getAmount());
        System.out.println(user.getOrder().getCart());
        user.save();
    }

    @Override
    public Type getType() {
        return Type.MEALS;
    }
}