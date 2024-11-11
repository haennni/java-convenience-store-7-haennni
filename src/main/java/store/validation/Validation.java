package store.validation;

import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;

public class Validation {
    public static void overInventoryQuantity(Order order, int productQuantity) {
        if (!(order.compareOrderQuantity(productQuantity))) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());
        }
    }
    public static void validateProductExists(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(Error.PRODUCT_NOT_FOUND.errorMsg());
        }
    }

    public static void validatePromotionExists(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException(Error.PROMOTION_NOT_FOUND.errorMsg());
        }
    }

    public static void validatePromotionProductExists(Product product) {
        if (product == null) {
            throw new IllegalArgumentException(Error.PROMOTION_PRODUCT_NOT_FOUND.errorMsg());
        }
    }
}
