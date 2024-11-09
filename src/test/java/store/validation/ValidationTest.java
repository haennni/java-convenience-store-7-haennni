package store.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Order;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
    Order order;
    int inventory;

    @BeforeEach
    void setUp() {
        order = new Order("콜라", 10);
        inventory = 10;
    }

    @Test
    @DisplayName("구매 수량이 재고 수량보다 많을 경우 예외가 발생한다.")
    void 구매수량이_재고수량보다_많을경우_예외처리() {
        assertThrows(IllegalArgumentException.class,
                () -> Validation.overInventoryQuantity(order, inventory), "구매 수량이 재고 수량보다 작습니다.");
    }
}