package ir.aminer.potadoshack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class PotadoShack extends Application {
    Map<String, String> pages = new HashMap<>()
    {{
        put("main_menu", "layouts/MainMenu.fxml");
        put("sign_in", "layouts/SignIn.fxml");
        put("sign_up", "layouts/SignUp.fxml");
        put("order_registration", "layouts/OrderRegistration.fxml");
        put("order_view", "layouts/OrderView.fxml");
        put("cart", "layouts/Cart.fxml");
    }};

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Potado Shack");

        /* Create Empty Scene */
        Scene primaryScene = new Scene(new Pane());
        primaryStage.setScene(primaryScene);

        /* Awake SceneHandler */
        SceneHandler.awoke(primaryScene);

        /* Dynamically add pages to SceneHandler */
        for (Map.Entry<String, String> page : pages.entrySet()){
            SceneHandler.getInstance().addNode(
                    page.getKey(),
                    FXMLLoader.load(getClass().getResource(page.getValue())));
        }

        /* Set current Page to MainMenu */
        SceneHandler.getInstance().activeNode("main_menu");

        primaryStage.show();
    }
}
