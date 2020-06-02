package ir.aminer.potadoshack.client;

import ir.aminer.potadoshack.client.controllers.SignUp;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class PotadoShack extends Application {

    Map<String, Page> pages = new HashMap<>()
    {{
        put("sign_up", new SignUp());
    }};

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Potado Shack");

        /* Create Empty Scene */
        Scene primaryScene = new Scene(new Pane());
        primaryStage.setScene(primaryScene);

        /* Awake PageHandler */
        PageHandler.awoke(primaryScene);

        /* Dynamically add pages to SceneHandler */
        for (Map.Entry<String, Page> page : pages.entrySet()){
            PageHandler.getInstance().addPage(
                    page.getKey(),
                    page.getValue());
        }

        /* Set current Page to MainMenu */
        SceneHandler.getInstance().activeNode("main_menu");

        if (!Client.hasPreference())
            SceneHandler.getInstance().activeNode("sign_up");

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
