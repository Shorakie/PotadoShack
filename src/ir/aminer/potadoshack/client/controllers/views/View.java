package ir.aminer.potadoshack.client.controllers.views;

import ir.aminer.potadoshack.client.controllers.MainMenu;

public abstract class View {

    public static enum Type{
        MEALS, CART, ORDERS, SETTINGS;
    }

    protected final MainMenu mainMenu;

    public View(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public abstract Type getType();
}
