package ir.aminer.potadoshack.client.controllers;

import ir.aminer.potadoshack.Main;
import ir.aminer.potadoshack.client.User;
import ir.aminer.potadoshack.client.controllers.custom.ProductCard;
import ir.aminer.potadoshack.client.page.Page;
import ir.aminer.potadoshack.core.network.ClientSocket;
import ir.aminer.potadoshack.core.network.packets.ViewCartPacket;
import ir.aminer.potadoshack.core.product.Food;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.Circle;

public class MainMenu extends Page {
    public TilePane pane;
    public ImageView img_user_pfp;

    public MainMenu() {
        super("layouts/MainMenu.fxml");
    }

    @Override
    public void onEnter(Page from) {
        try {
            ClientSocket client = new ClientSocket(Main.host, Main.port);

            ViewCartPacket viewCart = new ViewCartPacket(User.loadClient().getJwt().toString());
            client.sendPacket(viewCart);

            handleResponse(client);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        img_user_pfp.setClip(new Circle(img_user_pfp.getFitWidth()/2,img_user_pfp.getFitHeight()/2,img_user_pfp.getFitWidth()/2));
        for(Food food : Food.values()){
            ProductCard productCard = new ProductCard();
            productCard.setProduct(food);
            productCard.setOnOrder(this::onOrder);

            pane.getChildren().add(productCard);
        }
    }

    public void onOrder(ProductCard.OrderEvent event){
        System.out.println("Adding "+event.getAmount()+"*"+event.getProduct().getName());
        User user = User.loadClient();
        user.getCart().addProduct(event.getProduct(), event.getAmount());
        System.out.println(user.getCart());
        user.save();
    }

}
