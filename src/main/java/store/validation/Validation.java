package store.validation;

import store.domain.Order;

public class Validation {
    public static void overInventoryQuantity(Order order, int productQuantity) {
        if (!(order.compareOrderQuantity(productQuantity))) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());
        }
    }
}
