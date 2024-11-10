package store.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderAmountTest {

    @Test
    @DisplayName("OrderAmount 생성 시 총 가격이 올바르게 계산되는 지 테스트")
    void 총_가격_계산_테스트() {
        String productName = "콜라";
        int quantity = 3;
        int unitPrice = 1000;
        OrderAmount orderAmount = new OrderAmount(productName, quantity, unitPrice);

        assertEquals(productName, orderAmount.orderName(), "상품 이름이 올바르게 설정되어야 합니다.");
        assertEquals(quantity, orderAmount.totalQuantity(), "상품 수량이 올바르게 설정되어야 합니다.");
        assertEquals(3000, orderAmount.totalPrice(), "총 가격이 올바르게 계산되어야 합니다.");
    }
}