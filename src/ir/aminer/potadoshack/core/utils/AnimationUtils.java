package ir.aminer.potadoshack.core.utils;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationUtils {
    public static Transition pulse(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(90), node);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(1.04);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(4);
        scaleTransition.setInterpolator(Interpolator.EASE_OUT);
        return scaleTransition;
    }
}
