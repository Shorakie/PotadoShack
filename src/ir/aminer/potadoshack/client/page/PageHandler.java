package ir.aminer.potadoshack.client.page;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class PageHandler {

    private static PageHandler instance = null;

    private final Map<String, Page> pages = new HashMap<>();
    /* Default is an empty page */
    private Page currentPage = new Page(new Pane());
    private String currentTheme = "/themes/purple.css";
    private final Scene scene;

    private PageHandler(Scene scene) {
        this.scene = scene;
    }

    public void addPage(String name, Page page) {
        pages.put(name, page);
    }

    public void removePage(String name) {
        pages.remove(name);
    }

    public void activePage(String name) {
        if (currentPage == null)
            throw new IllegalStateException("Default page should be given");
        Page to = pages.get(name);

        /* Inform current page we are existing */
        currentPage.onExit(to);

        /* Change page */
        scene.setRoot(to.getParent());

        /* Inform dest page we are entering */
        to.onEnter(currentPage);

        /* set current page to dest */
        currentPage = to;
    }

    public void setPalette(String path) {
        for (Page page : pages.values()) {
            page.getParent().getStylesheets().remove(getClass().getResource(currentTheme).toExternalForm());
            page.getParent().getStylesheets().add(getClass().getResource(path).toExternalForm());
            currentTheme = path;
        }
    }

    public static void awake(Scene scene) {
        if (instance != null)
            throw new IllegalStateException("Instance is already initialized.");
        instance = new PageHandler(scene);
    }

    public static PageHandler getInstance() {
        if (instance == null)
            throw new IllegalStateException("Instance is not initialized.");

        return instance;
    }
}