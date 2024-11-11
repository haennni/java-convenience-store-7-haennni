package store.service;

import store.domain.Order;
import store.domain.Product;
import store.repository.ConvenienceStoreRepository;
import store.validation.Error;
import store.validation.Validation;

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
        Product product = products.stream()
                .filter(p -> p.productName().equals(order.orderName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Error.PRODUCT_NOT_FOUND.errorMsg()));
        if (product.productQuantity() == 0) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());}
        if (product.productQuantity() < order.orderQuantity()) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());}
        checkOrderEligibility(order, products);
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

    private void reduceRegularStock(Product product, int orderQuantity) {
        if (product.productQuantity() < orderQuantity) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg()); // Error Enum을 사용한 예외 메시지
        }
        product.deductionProductQuantity(orderQuantity);
    }
    
    private void reducePromoStock(Product product, int orderQuantity){
        int promoQuantity = product.productQuantity();
        if (promoQuantity >= orderQuantity) {
            product.deductionProductQuantity(orderQuantity);
            return;}
        int remainder = product.productPromotionRemainder(orderQuantity);
        product.promotionProductQuantityZero();
        Product regularProduct = retrieveRegularStockProduct(product.productName());
        handleInsufficientInventory(regularProduct, remainder);
        reduceRegularStock(regularProduct, remainder);
    }

    private static void  handleInsufficientInventory(Product regularProduct, int remainder) {
        if (regularProduct.productQuantity() < remainder) {
            // 일반 재고도 부족할 경우 예외 발생
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());
        }
    }

}
