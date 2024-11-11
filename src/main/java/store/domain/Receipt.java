package store.domain;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private final List<OrderAmount> orderAmounts = new ArrayList<>();
    private final List<Benefit> benefits = new ArrayList<>();
    private int originalAmount = 0;      // 총 금액
    private int benefitDiscount = 0;     // 프로모션 할인
    private int membershipDiscount = 0;  // 멤버십 할인
    private int finalAmount = 0;         // 최종 결제 금액
    private int finalQuantity = 0;       // 총 구매 수량

    public void addOrderAmount(OrderAmount orderAmount) {
        orderAmounts.add(orderAmount);
        originalAmount += orderAmount.totalPrice();   // 총 금액에 누적
        finalQuantity += orderAmount.totalQuantity(); // 총 수량에 누적 (가격과는 별개로)
    }

    public List<Benefit> benefits() {
        return benefits;
    }
    public void addBenefit(Benefit benefit) {
        benefits.add(benefit);
        benefitDiscount += benefit.benefitAmount();  // 프로모션 할인 금액 누적
        System.out.println("Benefit discount updated: " + benefitDiscount); // 누적 확인용
    }

    public void applyMembershipDiscount(int discount) {
        membershipDiscount = discount;
    }
    public void applyBenefitDiscount(int discount) {
        benefitDiscount = discount;  // 프로모션 할인 금액을 업데이트
    }
    public void calculateFinalAmount() {
        finalAmount = originalAmount - benefitDiscount - Math.min(membershipDiscount, 8000); // 멤버십 할인 최대 한도 8,000원 적용
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

    public void reduceBenefitDiscount(int price) {
        benefitDiscount =- price;
    }



    public int membershipDiscount() {
        return membershipDiscount;
    }

    public int finalAmount() {
        return finalAmount;
    }

    public int finalQuantity() {
        return finalQuantity;
    }

    public void totalQuantity() {
        for (OrderAmount orderAmount : orderAmounts) {
            finalQuantity += orderAmount.totalQuantity();
        }

    }
}
