package ir.aminer.potadoshack;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class SceneHandler {
    Map<String, Parent> nodes = new HashMap<>();
    Scene scene;

    public SceneHandler(Scene scene) {
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
}
