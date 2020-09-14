package ir.aminer.potadoshack.core.product;

public enum Drink implements Product {
    CAFE_CUBANO(1, "Cafe Cubano", 2000, "/images/drink/cold/cafe_cubano.png", Category.COLD_DRINK),
    ICED_COFFEE(2, "Iced Coffee", 2000, "/images/drink/cold/iced_coffee.png", Category.COLD_DRINK),
    LEMONADE(3, "Lemonade", 2000, "/images/drink/cold/lemonade.jpg", Category.COLD_DRINK),

    HOT_COFFEE(4, "Hot Coffee", 2000, "/images/drink/hot/hot_coffee.jpg", Category.HOT_DRINK),
    HOT_TEA(5, "Hot tea", 2000, "/images/drink/hot/hot_tea.jpg", Category.HOT_DRINK),

    STRAWBERRY_SHAKE(6, "Strawberry Shake", 8000, "/images/drink/shake/strawberry_shake.jpg", Category.SHAKE),
    BANANA_SHAKE(7, "Banana Shake", 8000, "/images/drink/shake/banana_shake.jpg", Category.SHAKE),
    OREO_SHAKE(8, "Oreo Shake", 8000, "/images/drink/shake/oreo_shake.jpg", Category.SHAKE),


    ;


    public enum Category implements Product.Category {
        HOT_DRINK("Hot drink", "orangered", "\f7b6"),
        COLD_DRINK("Cold drink", "skyblue", "\f0f4"),
        SHAKE("Shake", "hotpink", "\f0fc"),
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
            return this.icon;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    private final int id;
    private final String name;
    private final int price;
    private final String image_url;
    private final Category category;

    Drink(int id, String name, int price, String image_url, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.category = category;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Type getType() {
        return Type.DRINK;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public String getImageUrl() {
        return image_url;
    }

    @Override
    public Product.Category getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }
}
