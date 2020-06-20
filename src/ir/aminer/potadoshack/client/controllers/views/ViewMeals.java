package ir.aminer.potadoshack.client.controllers.views;

import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.client.controllers.custom.ProductCard;
import ir.aminer.potadoshack.client.controllers.custom.event.ProductChangeEvent;
import ir.aminer.potadoshack.core.product.Drink;
import ir.aminer.potadoshack.core.product.Food;
import ir.aminer.potadoshack.core.product.Product;
import ir.aminer.potadoshack.core.utils.Common;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ViewMeals extends View {
    @FXML
    private ScrollPane scroll_pane;
    @FXML
    private TilePane tile_pane;

    /// Search Section
    @FXML
    private ComboBox<String> meal_search;
    @FXML
    private ComboBox<Product.Category> category_search;
    @FXML
    private TextField name_search;
    @FXML
    private Button btn_search;

    private volatile boolean locked = false;

    final private Supplier<Task<Void>> updateMealsSupplier = () -> new Task<Void>() {
        @Override
        protected Void call() {
            /* Wait till the view is unlocked */
            while (locked)
                Thread.onSpinWait();

            /* Clear the tile pane */
            Platform.runLater(() -> tile_pane.getChildren().clear());

            Stream<Product> products = Stream.empty();
            if (meal_search.getValue().equalsIgnoreCase("all") || meal_search.getValue().equalsIgnoreCase("food"))
                products = Stream.concat(products, Arrays.stream(Food.values()));
            if (meal_search.getValue().equalsIgnoreCase("all") || meal_search.getValue().equalsIgnoreCase("drink"))
                products = Stream.concat(products, Arrays.stream(Drink.values()));

            lock();

            /* Add foods */
            products
                    /* Filter Category */
                    .filter(product -> {
                        if (category_search.getValue().getName().equalsIgnoreCase("all"))
                            return true;
                        return product.getCategory().equals(category_search.getValue());
                    })
                    /* Filter Name */
                    .filter(product ->
                            product.getName().toLowerCase().contains(name_search.getText().toLowerCase()))
                    /* Add to the tile pane */
                    .forEach(product -> {
                        if (isCancelled())
                            return;

                        ProductCard productCard = new ProductCard();
                        productCard.setProduct(product);
                        productCard.setOnOrder(ViewMeals.this::onOrder);


                        Platform.runLater(() -> tile_pane.getChildren().add(productCard));
                    });

            /* Queue unlocking after all cards have been added */
            Platform.runLater(() -> unlock());
            return null;
        }
    };

    private final static ExecutorService executorService =
            Common.createFixedTimeoutExecutorService(1, 1, TimeUnit.SECONDS);

    public ViewMeals(MainMenu mainMenu) {
        super(mainMenu);
    }

    @FXML
    public void initialize() {
        meal_search.getItems().addAll("All", "Food", "Drink");
        updateCategorySearch();

        executorService.submit(updateMealsSupplier.get());
    }

    @FXML
    public void dragging() {
        scroll_pane.setCursor(Cursor.OPEN_HAND);
    }

    private void updateCategorySearch() {
        category_search.getItems().clear();

        // Add a category named all
        category_search.getItems().add(new Product.Category() {
            @Override
            public String getName() {
                return "All";
            }

            @Override
            public String getColor() {
                return null;
            }

            @Override
            public String getIcon() {
                return null;
            }

            @Override
            public String toString() {
                return getName();
            }
        });

        if (meal_search.getValue().equalsIgnoreCase("food"))
            for (Food.Category foodCategory : Food.Category.values())
                category_search.getItems().add(foodCategory);
        else if (meal_search.getValue().equalsIgnoreCase("drink"))
            for (Drink.Category drinkCategory : Drink.Category.values())
                category_search.getItems().add(drinkCategory);

        category_search.setValue(category_search.getItems().get(0));
    }

    @FXML
    public void onMealChange() {
        updateCategorySearch();
    }

    @FXML
    public void onCategoryChange() {
        onSearch();
    }

    @FXML
    public void onSearch() {
        executorService.submit(updateMealsSupplier.get());
    }

    public void onOrder(ProductChangeEvent event) {
        System.out.println("Adding " + event.getAmount() + "*" + event.getProduct().getName());
        User user = User.loadClient();
        user.getOrder().getCart().addProduct(event.getProduct(), event.getAmount());
        user.save();
    }

    private void lock() {
        btn_search.setDisable(true);
        locked = true;
    }

    private void unlock() {
        btn_search.setDisable(false);
        locked = false;
    }

    @Override
    public Type getType() {
        return Type.MEALS;
    }
}