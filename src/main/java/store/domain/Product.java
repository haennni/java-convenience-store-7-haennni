package store.domain;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private String promotionName;

    public Product(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionName = promotion;
    }

    public String productName() {
        return name;
    }

    public int productPrice() {
        return price;
    }

    public int productQuantity() {
        return quantity;
    }

    public String productPromotionName() {
        return promotionName;
    }
}
