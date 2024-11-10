package store.domain;

public class Order {
    private final String name;
    private final int quantity;

    public Order(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String orderName() {
        return name;
    }

    public int orderQuantity() {
        return quantity;
    }

    public boolean compareOrderQuantity(int inventoryQuantity) {
        return inventoryQuantity > quantity;
    }
}
