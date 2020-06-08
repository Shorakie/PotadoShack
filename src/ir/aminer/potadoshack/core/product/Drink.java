package ir.aminer.potadoshack.core.product;

public enum Drink implements Product {
    WATER(1, "Water", 2000, "https://p7.hiclipart.com/preview/818/930/517/cartoon-beverage-bottles.jpg", Category.COLD_DRINK);


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
