package ir.aminer.potadoshack.core.utils;

import javafx.animation.FadeTransition;
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

    public static Transition fade(Node node, boolean out) {
        FadeTransition transition = new FadeTransition(Duration.millis(140), node);
        if (out)
            transition.setToValue(0);
        else
            transition.setToValue(1);
        return transition;
    }
}
