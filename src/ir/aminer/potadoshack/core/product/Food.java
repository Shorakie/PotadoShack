package ir.aminer.potadoshack.core.product;

public enum Food implements Product {
    /* Burgers */
    BURGER(1, "Burger", 8000, "Bread, Burger", "/girl-1.png", Category.BURGER),
    CHEESE_BURGER(2, "Cheese Burger", 8000, "Bread, Burger, Cheese", "/girl-2.jpg", Category.BURGER),
    MUSHROOM_BURGER(3, "Mushroom Burger", 8000, "Bread, Burger, Mushroom", "/girl-3.jpg", Category.BURGER),
    ROYAL_BURGER(4, "Royal Burger", 8000, "Bread, Burger", "/girl-4.png", Category.BURGER),
    TANDOORI_DOUBLE_BURGER(5, "TANDOORI Double Burger", 8000, "Bread, Burger", "/girl-5.jpg", Category.BURGER),
    TANDOORI_BURGER(6, "TANDOORI Burger", 8000, "Bread, Burger", "/girl-6.png", Category.BURGER),


    ;
    public enum Category implements Product.Category {
        APPETIZER("Appetizer", "orange"),
        BURGER("Burger", "saddlebrown"),
        SANDWICH("Sandwich", "sandybrown"),
        CUTLET("Cutlet", "goldenrod"),
        TANDOORI("Tandoori", "darkred"),

        ;

        private final String name;
        private final String color;

        Category(String name, String color) {
            this.name = name;
            this.color = color;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public String getColor() {
            return this.color;
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
