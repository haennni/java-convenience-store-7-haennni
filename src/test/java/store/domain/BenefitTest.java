package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BenefitTest {
    private Benefit benefit;

    @BeforeEach
    void setUp() {
        benefit = new Benefit("콜라", 2, 1000);
    }

    @Test
    @DisplayName("Benefit 객체 생성 시 초기화 테스트")
    void testBenefitInitialization() {
        assertEquals("콜라", benefit.benefitProductName(), "상품명이 올바르게 초기화되어야 합니다.");
        assertEquals(2, benefit.benefitQuantity(), "수량이 올바르게 초기화되어야 합니다.");
        assertEquals(0, benefit.benefitAmount(), "초기 금액이 0으로 초기화되어야 합니다.");
    }

    @Test
    @DisplayName("혜택을 받은 상품의 수량을 증가시켰을 때 +1 되는 지 확인하는 테스트")
    void 혜택받은_상품의_수량이_증가하였는지_테스트() {
        assertEquals(2, benefit.benefitQuantity(), "초기 혜택 수량이 2이어야 합니다.");
        benefit.plusBenefitQuantity(1000);
        assertEquals(3, benefit.benefitQuantity(), "호출 후 혜택 수량이 1 증가해야 합니다.");
        assertEquals(3000, benefit.benefitAmount(), "호출 후 혜택 금액이 증가해야 합니다.");
    }

    @Test
    @DisplayName("금액 반환 테스트")
    void testBenefitAmount() {
        assertEquals(2000, benefit.benefitAmount(), "혜택 금액이 일치하지않습니다.");
    }
}