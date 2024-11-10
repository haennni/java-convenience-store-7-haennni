package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    Order order;

    @BeforeEach
    void setUp() {
        order = new Order("감자칩", 3);
    }

    @Test
    @DisplayName("해당하는 주문의 상품명을 가져오는 메서드 테스트")
    void 주문이름_가져오기_테스트() {
        String orderName = order.orderName();
        assertEquals("감자칩", orderName);
    }

    @Test
    @DisplayName("해당하는 주문의 상품명을 가져오는 메서드 테스트")
    void 주문수량_가져오기_테스트() {
        int orderQuantity = order.orderQuantity();
        assertEquals(3, orderQuantity);
    }

    @Test
    @DisplayName("해당 주문이 상품 저장소의 재고수량보다 적은지 확인하는 테스트")
    void 주문수량_재고_확인_테스트() {
        assertTrue(order.compareOrderQuantity(4));
        assertFalse(order.compareOrderQuantity(2));
    }
}