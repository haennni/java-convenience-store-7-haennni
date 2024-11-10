package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<OrderAmount> orderAmounts = new ArrayList<>();
    private final List<Benefit> benefits = new ArrayList<>();
    private int originalAmount;
    private int benefitDiscount;
    private int membershipDiscount;
    private int finalAmount;

    public void addOrderAmount(OrderAmount orderAmount) {
        orderAmounts.add(orderAmount);
        System.out.println("Adding OrderAmount: " + orderAmount.totalPrice()); // 확인용 출력
        originalAmount += orderAmount.totalPrice();
        System.out.println("Updated Original Amount: " + originalAmount); // 누적 값 출력
    }

    public List<Benefit> benefits() {
        return benefits;
    }
    public void addBenefit(Benefit benefit) {
        benefits.add(benefit);
    }
    public void applyBenefitDiscount(int discount) {
        benefitDiscount = discount;
    }
    public void applyMembershipDiscount(int discount) {
        membershipDiscount = discount;
    }
    public void calculateFinalAmount() {
        finalAmount = originalAmount - benefitDiscount - membershipDiscount;
    }

    public int originalAmount() {
        return originalAmount;
    }

    public List<OrderAmount> orderAmounts() {
        return orderAmounts;
    }

    public int benefitDiscount() {
        return benefitDiscount;
    }

    public int membershipDiscount() {
        return membershipDiscount;
    }

    public int finalAmount() {
        return finalAmount;
    }
}
