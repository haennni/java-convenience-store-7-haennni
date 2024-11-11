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

    /* 재고 차감 */
    public void deductionProductQuantity(int orderQuantity) {
        quantity -= orderQuantity;
    }
    /* 재고 0개로 만들기 */
    public int productPromotionRemainder(int orderQuantity) {
        return orderQuantity - quantity;
    }

    public void promotionProductQuantityZero() {
        quantity = 0;
    }

    public String getFormattedQuantity() {
        if (quantity > 0) {
            return quantity + "개";
        }
        return "재고 없음";
    }

    // 상품 정보를 포맷팅하여 출력
    public String formattingProduct() {
        String formattedPrice = String.format("%,d", price);
        String formattedPromotionName = "";
        if (promotionName != null) {
            formattedPromotionName = " " + promotionName;
        }
        return String.format("- %s %s원 %s%s", name, formattedPrice, getFormattedQuantity(), formattedPromotionName);
    }

}
