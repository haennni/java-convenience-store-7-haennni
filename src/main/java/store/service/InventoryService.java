package store.service;

import store.domain.Order;
import store.domain.Product;
import store.repository.ConvenienceStoreRepository;
import store.validation.Validation;

import java.util.List;

public class InventoryService {

    /* 1. 재고수량과 구매 수량을 파악하여 결제 가능 여부를 파악한다.*/
    public void checkInventory(ConvenienceStoreRepository convenienceStoreRepository) {
        List<Product> products = convenienceStoreRepository.products();
        List<Order> orders = convenienceStoreRepository.orders();
        checkOrderEligibility(orders, products);
    }

    private static void checkOrderEligibility(List<Order> orders, List<Product> products) {
        for (Order order : orders)
            products.stream().filter(p -> p.productName().equals(order.orderName()))
                    .forEach(p -> Validation.overInventoryQuantity(order, p.productQuantity()));
    }
}
