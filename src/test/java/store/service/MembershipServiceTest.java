package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.OrderAmount;
import store.domain.Receipt;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MembershipServiceTest {

    private MembershipService membershipService;
    private Receipt receipt;

    @BeforeEach
    void setUp() {
        membershipService = new MembershipService();
        receipt = new Receipt();
    }

    @Test
    @DisplayName("프로모션 할인 후 금액의 30%가 멤버십 할인으로 적용되는지 확인하는 테스트")
    void 멤버십_할인_적용_테스트() {
        receipt.addOrderAmount(new OrderAmount("콜라", 3, 1000)); // 총 금액: 5000원
        receipt.addOrderAmount(new OrderAmount("에너지바", 5, 2000)); // 총 금액: 5000원*/
        receipt.applyBenefitDiscount(1000); // 예시로 프로모션 할인을 1000원 적용
        assertEquals(3000, receipt.orderAmounts().get(0).totalPrice(), "총 가격은 3,000원이여야합니다.");
        assertEquals(10000, receipt.orderAmounts().get(1).totalPrice(), "총 가격은 3,000원이여야합니다.");
        assertEquals(13000, receipt.originalAmount(), "총 가격은 13,000원입니다.");
        int discount = membershipService.calculateMembershipDiscount(receipt);

        // 프로모션 할인 후 금액이 3000원이므로, 30%인 900원이 할인되어야 함
        assertEquals(3900, discount, "멤버십 할인 금액이 올바르게 계산되어야 함");
        assertThat(receipt.membershipDiscount()).isEqualTo(3900);
    }

    @Test
    @DisplayName("멤버십 할인이 최대 8000원으로 제한됬는지 확인하는 테스트")
    void 멤버십_할인_한도_테스트() {
        receipt.applyBenefitDiscount(5000); // 예시로 프로모션 할인을 5000원 적용
        receipt.addOrderAmount(new OrderAmount("바나나", 10, 1000)); // 총 금액: 10000원
        receipt.addOrderAmount(new OrderAmount("에너지바", 10, 2000)); // 총 금액: 20000원*/

        int discount = membershipService.calculateMembershipDiscount(receipt);

        // 최대 할인 한도가 8000원이므로, 8000원으로 제한되어야 함
        assertEquals(8000, discount, "멤버십 할인 한도가 초과되지 않아야 함");
        assertThat(receipt.membershipDiscount()).isEqualTo(8000);
    }

}