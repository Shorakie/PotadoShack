package ir.aminer.potadoshack.core.product;

public enum Food implements Product {
    /* Burgers */
    BURGER(1, "Burger", 8000, "Bread, Burger", "/images/food/burgers/burger.jpg", Category.BURGER),
    CHEESE_BURGER(2, "Cheese Burger", 10000, "Bread, Burger, Cheese", "/images/food/burgers/cheese_burger.jpg", Category.BURGER),
    DOUBLE_BURGER(4, "Double Burger", 12000, "Bread, Burgerx2", "/images/food/burgers/double_burger.png", Category.BURGER),
    TANDOORI_BURGER(5, "TANDOORI Burger", 13000, "Bread, Burger, Cheese", "/images/food/burgers/tandori_burger.jpg", Category.BURGER),

    HOTDOG(6, "Hotdog", 12000, "Bread, Hotdog", "/images/food/hotdogs/hotdog.jpg", Category.HOTDOG),
    TANDORI_HOTDOG(7, "Tandori Hotdog", 16000, "Bread, Hotdog, Cheese", "/images/food/hotdogs/tandori_hotdog.png", Category.HOTDOG),

    CAESAR_SALAD(9, "Caesar Salad", 2000, "Vegetables", "/images/food/appetizers/caesar_salad.png", Category.APPETIZER),
    CRAB_CAKE(10, "Crab Cake", 4000, "Crab", "/images/food/appetizers/crab_cake.png", Category.APPETIZER),
    ONION_DIP(11, "Onion Dip", 4000, "Onion, yogurt", "/images/food/appetizers/onion_dip.png", Category.APPETIZER),
    THREECHEESE_MAC(12, "Three-Cheese Mac", 6000, "mac, cheese", "/images/food/appetizers/three-cheese_mac.png", Category.APPETIZER),

    PIZZA(12, "Pizza", 12000, "Cheese, Tomato, Pizza bread", "/images/food/pizzas/pizza.jpg", Category.PIZZA),
    MUSHROOM_PIZZA(13, "Caesar Salad", 14000, "Cheese, Tomato, Pizza bread, Mushroom", "/images/food/pizzas/mushroom_pizza.jpg", Category.PIZZA),
    SPICY_PIZZA(14, "Caesar Salad", 14000, "Cheese, Tomato, Pizza bread, Spice", "/images/food/pizzas/spicy_pizza.jpg", Category.PIZZA),
    ITALIAN_PIZZA(15, "Caesar Salad", 16000, "Cheese, Tomato, Pizza bread", "/images/food/pizzas/italian_pizza.jpg", Category.PIZZA),
    MINI_PIZZA(15, "Caesar Salad", 7000, "Cheese, Tomato, Pizza bread", "/images/food/pizzas/mini_pizza.jpg", Category.PIZZA),
    ;

    public enum Category implements Product.Category {
        APPETIZER("Appetizer", "#b2ff59", "\uf06c"),
        BURGER("Burger", "#795548", "\uf805"),
        HOTDOG("Hotdog", "#ff6e40", "\uf80f"),
        PIZZA("Pizza", "#ffab40", "\uf818"),
        ;

        private final String name;
        private final String color;
        private final String icon;

        Category(String name, String color, String icon) {
            this.name = name;
            this.color = color;
            this.icon = icon;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getColor() {
            return this.color;
        }

        @Override
        public String getIcon() {
            return icon;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    private final int id;
    private final String name;
    private final int price;
    private final String ingredients;
    private final String image_url;
    private final Category category;

    Food(int id, String name, int price, String ingredients, String image_url, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.image_url = image_url;
        this.category = category;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Type getType() {
        return Type.FOOD;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public String getIngredients() {
        return ingredients;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return image_url;
    }

    @Override
    public Category getCategory() {
        return category;
    }

}