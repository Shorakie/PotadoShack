package ir.aminer.potadoshack.core.product;

import java.io.Serializable;

public interface Product extends Serializable {
    public enum Type {
        FOOD, DRINK;
    }

    public interface Category {
        public String getName();

        public String getColor();

        public String getIcon();
    }

    public int getId();

    public Type getType();

    public String getName();

    public int getPrice();

    public String getImageUrl();

    public Category getCategory();
}
