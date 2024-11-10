package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.Benefit;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.view.InputView;

import java.time.LocalDateTime;
import java.util.List;

import static store.repository.ConvenienceStoreRepository.*;

public class PromotionService {

    private final InputView inputView;

    public PromotionService(InputView inputView) {
        this.inputView = inputView;
    }

    public void checkPromotion(Order order) {
        Product product = eilgiblePromotionProduct(order);
        Promotion promotion = findPromotion(product.productPromotionName());
        if (promotionDateValid(promotion)) {
            if (promotion.promotionName().equals("탄산2+1"))
                twoPlusOnePromotion(product, promotion, order);
            if (promotion.promotionName().equals("MD추천상품") || promotion.promotionName().equals("반짝할인"))
                onePlusOnePromotion(product, promotion, order);
        }
    }

    // 프로모션 기간 검사
    public boolean promotionDateValid(Promotion promotion) {
        LocalDateTime orderTime = DateTimes.now();
        LocalDateTime promotionStartDate = toDate(promotion.promotionStartDate());
        LocalDateTime promotionEndDate = toDate(promotion.promotionEndDate());
        // orderTime이 시작일 이상이고 종료일 이하일 때 true 반환
        return (orderTime.isEqual(promotionStartDate) || orderTime.isAfter(promotionStartDate)) &&
                (orderTime.isEqual(promotionEndDate) || orderTime.isBefore(promotionEndDate));
    }


    private Product eilgiblePromotionProduct(Order order) {
        return products.stream().filter(p -> p.productName().equals(order.orderName()))
                .filter(p -> !(p.productPromotionName() == null))
                .filter(p -> !(p.productQuantity() == 0))
                .findFirst().orElseThrow();
    }

    private Promotion findPromotion(String promotionName) {
        return promotions.stream().filter(p -> p.promotionName().equals(promotionName))
                .findFirst().orElseThrow();
    }

    // 1+1 Promotion 로직
    void onePlusOnePromotion(Product product, Promotion promotion, Order order) {
        if (product.productQuantity() > order.orderQuantity()) {
            handleOnePlusOnePromotionRemainder(product, promotion, order);
            return;
        }
        handleOnePlusOneNonFullPromotion(product, promotion, order);
    }

    // 1+1의 나머지 처리를 담당하는 메서드
    private void handleOnePlusOnePromotionRemainder(Product product, Promotion promotion, Order order) {
        int remainder = order.orderQuantity() % 2;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        if (remainder == 1) {
            applyPromotionIfAgreed(order, promotion, true, 1, product.productPrice());
        }
    }

    // 주문 수량이 프로모션 기준을 넘었을 때 남은 수량을 처리하는 메서드
    private void handleOnePlusOneNonFullPromotion(Product product, Promotion promotion, Order order) {
        int remainder = product.productQuantity() % 2;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        int regularQuantity = order.orderQuantity() - product.productQuantity() + remainder;
        if (regularQuantity > 0)
            applyPromotionIfAgreed(order, promotion, true, regularQuantity, product.productPrice());
    }

    // 2+1 Promotion 로직
    void twoPlusOnePromotion(Product product, Promotion promotion, Order order) {
        // 주문 수량이 재고 갯수보다 적을 때
        if (product.productQuantity() > order.orderQuantity()) {
            handleTwoPlusOnePromotionRemainder(order, product, promotion, 3);
            return;
        }
        // 주문 수량이 프로모션 조건과 같거나 많을 때
        handleTwoPlusOneNonFullPromotion(order, product, promotion, 3);
    }

    // 2+1프로모션의 나머지를 처리하는 메서드
    private void handleTwoPlusOnePromotionRemainder(Order order, Product product, Promotion promotion, int promotionSize) {
        int remainder = order.orderQuantity() % promotionSize;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        if (remainder == 1) {
            applyPromotionIfAgreed(order, promotion, true, 1, product.productPrice());
            return;
        }
        if (remainder == 2)
            applyPromotionIfAgreed(order, promotion, false, 2, product.productPrice());
    }

    // 2+1주문 수량이 프로모션 조건과 같거나 많을 때 남은 수량을 처리하는 메서드
    private void handleTwoPlusOneNonFullPromotion(Order order, Product product, Promotion promotion, int promotionSize) {
        int notBenefit = order.orderQuantity() % promotionSize;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        if (notBenefit != 0) {
            int remainder = product.productPromotionRemainder(order.orderQuantity()) + notBenefit;
            applyPromotionIfAgreed(order, promotion, true, remainder, product.productPrice());
        }
    }

    // 고객 동의를 받아 프로모션을 적용하는 메서드
    void applyPromotionIfAgreed(Order order, Promotion promotion, boolean isInventoryLow, int remainder, int price) {
        String answerBenefit;
        if (isInventoryLow) {
            answerBenefit = inputView.promotionInventoryLowInput(order.orderName(), remainder);
            addAdditionalPurchase(order, answerBenefit, price);
            return;
        }
        answerBenefit = inputView.promotionFreeBenefitInput(order.orderName(), promotion.promotionGet());
        addAdditionalPurchase(order, answerBenefit, price);
    }

    void addAdditionalPurchase(Order order, String answer, int benefitPrice) {
        if (answer.equals("Y")){
            order.plusOrderQuantity();
            List<Benefit> benefits = receipt.benefits();
            benefits.stream().filter(b ->
                    b.benefitProductName().equals(order.orderName()))
                    .forEach(b -> b.plusBenefitQuantity(benefitPrice));
        }
    }

    private LocalDateTime toDate(String date) {
        String[] splitDate = date.split("-");
        int year = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int day = Integer.parseInt(splitDate[2]);
        return LocalDateTime.of(year, month, day, 0, 0, 0);
    }

    private static void createBenefit(Product product, Order order, int promotionSize) {
        int benefitQuantity = order.orderQuantity() / promotionSize;
        Benefit benefit = new Benefit(order.orderName(), benefitQuantity, product.productPrice());
        receipt.addBenefit(benefit);
    }

}