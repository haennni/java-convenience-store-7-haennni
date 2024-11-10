package store.repository;

import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.util.InputParser;
import store.view.InputView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvenienceStoreRepository {
    public static List<Product> products;
    public final static List<Order> orders = new ArrayList<>();
    public final static List<Promotion> promotions = new ArrayList<>();

    ConvenienceStoreRepository(ProductFactory productFactory, PromotionFactory promotionFactory,
                               OrderFactory orderFactory, InputView inputView, InputParser inputParser) throws IOException {
        products = productFactory.toProducts();
        String orderInput = inputView.orderInput();
        List<String[]> orders = inputParser.orderParser(orderInput);
        orderFactory.toOrders(orders);
        promotionFactory.toPromotions();
    }

    public List<Product> products() {
        return products;
    }
    public List<Order> orders() {
        return orders;
    }
    public List<Promotion> promotions() {
        return promotions;
    }
}
