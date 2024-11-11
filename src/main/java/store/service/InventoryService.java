package store.service;

import store.domain.Order;
import store.domain.Product;
import store.repository.ConvenienceStoreRepository;
import store.validation.Error;
import store.validation.Validation;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;
import java.util.Optional;

import static store.repository.ConvenienceStoreRepository.orders;
import static store.repository.ConvenienceStoreRepository.products;

public class InventoryService {

    private final PromotionService promotionService;

    public InventoryService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
    public void checkInventory(Order order) {
        Product promoProduct = findPromotionProduct(order.orderName());
        Product regularProduct = findRegularProduct(order.orderName());
        int remainingQuantity = deductPromoStockIfAvailable(promoProduct, order.orderQuantity());
        deductRegularStockWithUserConfirmation(regularProduct, remainingQuantity, order.orderName());
    }

    private int deductPromoStockIfAvailable(Product promoProduct, int requiredQuantity) {
        if (promoProduct == null || promoProduct.productQuantity() <= 0) {
            return requiredQuantity;
        }
        int promoQuantity = promoProduct.productQuantity();
        if (promoQuantity >= requiredQuantity) {
            reducePromoStock(promoProduct, requiredQuantity);
            return 0;
        }
        reducePromoStock(promoProduct, promoQuantity);
        return requiredQuantity - promoQuantity;
    }

    private void deductRegularStockWithUserConfirmation(Product regularProduct, int requiredQuantity, String productName) {
        if (requiredQuantity <= 0) {
            return;
        }
        if (regularProduct.productQuantity() < requiredQuantity) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());
        }
        if (userConfirmsRegularPrice(productName, requiredQuantity)) {
            reduceRegularStock(regularProduct, requiredQuantity);
        }
    }

/*    private boolean userConfirmsRegularPrice(String productName, int remainingQuantity) {
        String answer = InputView.promotionInventoryLowInput(productName, remainingQuantity);
        return answer.equalsIgnoreCase("Y");
    }*/

    private boolean userConfirmsRegularPrice(String productName, int remainingQuantity) {
        String answer = InputView.promotionInventoryLowInput(productName, remainingQuantity);
        if(answer.equals("Y")){
            return true;
        }
        return false;
    }
    private Product findPromotionProduct(String productName) {
        return products.stream()
                .filter(p -> p.productName().equals(productName) && p.productPromotionName() != null)
                .findFirst()
                .orElse(null);
    }

    private Product findRegularProduct(String productName) {
        return products.stream()
                .filter(p -> p.productName().equals(productName) && p.productPromotionName() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Error.PRODUCT_NOT_FOUND.errorMsg()));
    }

    public void checkOrderEligibility(Order order, List<Product> products) {
        products.stream()
                .filter(p -> p.productName().equals(order.orderName()))
                .forEach(p -> checkPromotionQuantity(p, order.orderQuantity()));
    }

    public void checkPromotionQuantity(Product product, int orderQuantity) {
        if (product.productPromotionName() != null && product.productQuantity() > 0)  {
            reducePromoStock(product, orderQuantity);
            return;
        }
        reduceRegularStock(product, orderQuantity);
    }
    private Product retrieveRegularStockProduct(String productName) {
        Product regularProduct = products.stream()
                .filter(p -> p.productName().equals(productName))
                .filter(p -> p.productPromotionName() == null)
                .findFirst()
                .orElse(null);
        Validation.validateProductExists(regularProduct);
        return regularProduct;
    }

    private void reduceRegularStock(Product product, int quantity) {
        product.deductionProductQuantity(quantity);
    }

    private void reducePromoStock(Product product, int quantity) {
        product.deductionProductQuantity(quantity);
    }

    private static void  handleInsufficientInventory(Product regularProduct, int remainder) {
        if (regularProduct.productQuantity() < remainder) {
            // 일반 재고도 부족할 경우 예외 발생
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());
        }
    }

}
