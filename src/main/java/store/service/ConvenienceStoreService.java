package store.service;

import store.domain.Order;
import store.domain.OrderAmount;
import store.domain.Product;
import store.repository.ConvenienceStoreRepository;
import store.validation.Error;
import store.view.OutputView;

import java.util.Optional;

import static store.repository.ConvenienceStoreRepository.*;

public class ConvenienceStoreService {
    private final ConvenienceStoreRepository repository;
    private final InventoryService inventoryService;
    private final PromotionService promotionService;
    private final OutputView outputView;

    public ConvenienceStoreService(ConvenienceStoreRepository repository, InventoryService inventoryService, PromotionService promotionService, OutputView outputView) {
        this.repository = repository;
        this.inventoryService = inventoryService;
        this.promotionService = promotionService;
        this.outputView = outputView;
    }

    public void run() {
        for (Order order : orders) {
            Optional<Product> optionalProduct = findProductByName(order.orderName());
            if (optionalProduct.isEmpty())
                throw new IllegalArgumentException(Error.PRODUCT_NOT_FOUND.errorMsg());
            Product product = optionalProduct.get();
            processOrder(order, product);
            calculateTotalOrderAmount(order);}
        receipt.calculateFinalAmount();
    }

    private Optional<Product> findProductByName(String orderName) {
        return products.stream()
                .filter(p -> p.productName().equals(orderName))
                .findFirst();
    }

    private void processOrder(Order order, Product product) {
        inventoryService.checkInventory(order);

        if (product.productPromotionName() == null) {
            inventoryService.checkInventory(order);
            return;
        }
        if (product.productQuantity() > 0) {
            promotionService.checkPromotion(order);
            return;
        }
    }

    private int productPrice(String orderName) {
        return findProductByName(orderName)
                .map(Product::productPrice)
                .orElse(0);
    }

    public int calculateTotalOrderAmount(Order order) {
        OrderAmount orderAmount = new OrderAmount(order.orderName(), order.orderQuantity(), productPrice(order.orderName()));
        receipt.addOrderAmount(orderAmount);
        return receipt.originalAmount();
    }
}
