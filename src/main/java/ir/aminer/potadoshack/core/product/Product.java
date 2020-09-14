package ir.aminer.potadoshack.core.product;

import java.io.Serializable;

public interface Product extends Serializable {
    enum Type {
        FOOD, DRINK
    }

    interface Category {
        String getName();

        String getColor();

        String getIcon();
    }

    int getId();

    Type getType();

    String getName();

    int getPrice();

    String getImageUrl();

    Category getCategory();
}
