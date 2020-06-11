package ir.aminer.potadoshack.client;

import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.controllers.MainMenu;
import ir.aminer.potadoshack.client.controllers.SignIn;
import ir.aminer.potadoshack.client.controllers.SignUp;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.client.page.PageHandler;
import ir.aminer.potadoshack.core.order.Address;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.Map;

public class PotadoShack extends Application {

    Map<String, Page> pages = new HashMap<>()
    {{
        put("main_menu", new MainMenu());
        put("sign_in", new SignIn());
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

        if (!User.hasPreference())
            PageHandler.getInstance().activePage("sign_up");
        else
            PageHandler.getInstance().activePage("sign_in");

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }
}
