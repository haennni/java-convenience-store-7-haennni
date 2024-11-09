package store.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderFactoryTest {

    OrderFactory orderFactory;

    @BeforeEach
    void setUp() {
        orderFactory = new OrderFactory();
    }

    @Test
    void 하나의_주문_생성_테스트() {
        String[] input = {"콜라", "10"};
        Order order = orderFactory.toOrder(input);
        assertEquals("콜라", order.orderName());
        assertEquals(10, order.orderQuantity());
    }

    @Test
    void 주문리스트_생성_테스트() {
        List<String[]> inputOrders = List.of(
                new String[]{"콜라", "10"},
                new String[]{"바나나", "20"}
        );

        List<Order> orders = orderFactory.toOrders(inputOrders);

        assertEquals(orders.size(), 2, "주문 리스트 사이즈가 일치하지않습니다.");
        assertEquals("콜라", orders.get(0).orderName(), "첫 번째 주문의 상품명이 일치하지않습니다.");
        assertEquals(10, orders.get(0).orderQuantity(), "첫 번째 주문의 수량이 일치하지않습니다.");
        assertEquals("바나나", orders.get(1).orderName(), "두 번째 주문의 상품명이 일치하지않습니다.");
        assertEquals(20, orders.get(1).orderQuantity(), "두 번째 주문의 수량이 일치하지않습니다.");
    }
}