package store.domain;

public class OrderAmount {
    private final String orderName;
    private final int totalQuantity;
    private final int totalPrice;

    public OrderAmount(String orderName, int totalQuantity, int unitPrice) {
        this.orderName = orderName;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalQuantity * unitPrice;
    }

    public String orderName() {
        return orderName;
    }

    public int totalQuantity() {
        return totalQuantity;
    }

    public int totalPrice() {
        return totalPrice;
    }
}
