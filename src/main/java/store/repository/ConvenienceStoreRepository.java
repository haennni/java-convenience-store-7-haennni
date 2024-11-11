package store.repository;

import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.util.InputParser;
import store.view.InputView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConvenienceStoreRepository {
    public static Receipt receipt = new Receipt();
    public static List<Product> products = new ArrayList<>();
    public static List<Order> orders = new ArrayList<>();
    public final static List<Promotion> promotions = new ArrayList<>();

    public ConvenienceStoreRepository(ProductFactory productFactory, PromotionFactory promotionFactory,
                                      OrderFactory orderFactory, InputParser inputParser) throws IOException {
        products = productFactory.toProducts();
        promotionFactory.toPromotions();
    }

    public void resetReceipt() {
        receipt = new Receipt();  // 새 객체를 할당하여 초기화합니다.
    }

    public void loadOrders(InputView inputView, InputParser inputParser) {
        String orderInput = inputView.orderInput();
        List<String[]> orderData = inputParser.orderParser(orderInput);
        orders = OrderFactory.toOrders(orderData);
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
