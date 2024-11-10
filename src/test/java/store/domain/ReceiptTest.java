package store.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptTest {

    private Receipt receipt;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
    }

    @Test
    @DisplayName("주문 항목 추가 및 원래 금액 계산 테스트")
    void 주문_항목_추가_및_원래_금액_계산_테스트() {
        OrderAmount order1 = new OrderAmount("콜라", 3, 1000);
        OrderAmount order2 = new OrderAmount("사이다", 2, 1000);
        receipt.addOrderAmount(order1);
        receipt.addOrderAmount(order2);
        assertEquals(5000, receipt.originalAmount(), "주문 항목 추가 후 원래 금액이 올바르게 계산되어야 합니다.");
    }

    @Test
    @DisplayName("혜택 추가 테스트")
    void 혜택_추가_테스트() {
        Benefit benefit = new Benefit("콜라", 1, 1000);
        receipt.addBenefit(benefit);
        assertEquals(1, receipt.benefits().size(), "혜택 목록에 혜택이 올바르게 추가되어야 합니다.");
        assertEquals("콜라", receipt.benefits().get(0).benefitProductName(), "추가된 혜택의 상품명이 올바르게 저장되어야 합니다.");
    }

    @Test
    @DisplayName("혜택 할인 적용 테스트")
    void 혜택_할인_적용_테스트() {
        receipt.applyBenefitDiscount(1000);
        assertEquals(1000, receipt.benefitDiscount(), "혜택 할인이 올바르게 적용되어야 합니다.");
    }

    @Test
    @DisplayName("멤버십 할인 적용 테스트")
    void 멤버십_할인_적용_테스트() {
        receipt.applyMembershipDiscount(2000);
        assertEquals(2000, receipt.membershipDiscount(), "멤버십 할인이 올바르게 적용되어야 합니다.");
    }

    @Test
    @DisplayName("최종 금액 계산 테스트")
    void 최종_금액_계산_테스트() {
        OrderAmount order1 = new OrderAmount("콜라", 3, 1000);
        OrderAmount order2 = new OrderAmount("사이다", 2, 1000);
        receipt.addOrderAmount(order1);
        receipt.addOrderAmount(order2);
        receipt.applyBenefitDiscount(500);
        receipt.applyMembershipDiscount(1000);
        receipt.calculateFinalAmount();
        assertEquals(3500, receipt.finalAmount(), "최종 결제 금액이 올바르게 계산되어야 합니다.");
    }
}
