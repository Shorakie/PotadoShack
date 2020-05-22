package ir.aminer.potadoshack;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class SceneHandler {

    private static SceneHandler instance = null;

    Map<String, Parent> nodes = new HashMap<>();
    Scene scene;

    private SceneHandler(Scene scene) {
        this.scene = scene;
    }

    public void addNode(String name, Parent node) {
        nodes.put(name, node);
    }

    public void removeNode(String name) {
        nodes.remove(name);
    }

    public void activeNode(String name) {
        scene.setRoot(nodes.get(name));
    }

    public static void awoke(Scene scene) {
        if (instance != null)
            throw new IllegalStateException("Instance is already initialized.");
        instance = new SceneHandler(scene);
    }

    public static SceneHandler getInstance() {
        if (instance == null)
            throw new IllegalStateException("Instance is not initialized.");

        return instance;
    }
}
