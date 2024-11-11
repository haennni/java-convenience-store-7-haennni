package store.service;

import store.domain.Receipt;

public class MembershipService {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;

    public int calculateMembershipDiscount(Receipt receipt) {
        // 프로모션 적용 전 금액에 대해 30% 할인 계산
        int membershipDiscount = (int) (receipt.originalAmount() * MEMBERSHIP_DISCOUNT_RATE);
        if (membershipDiscount > MAX_DISCOUNT) {
            membershipDiscount = MAX_DISCOUNT;}
        receipt.applyMembershipDiscount(membershipDiscount);
        return membershipDiscount;

    }
}