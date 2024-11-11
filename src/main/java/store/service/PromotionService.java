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
        Product product = findEligiblePromotionProduct(order);
        Promotion promotion = findPromotion(product.productPromotionName());

        if (promotion != null && promotionDateValid(promotion)) {
            applyPromotion(product, promotion, order);
        }
    }

    private Product findEligiblePromotionProduct(Order order) {
        return products.stream()
                .filter(p -> p.productName().equals(order.orderName()) && p.productPromotionName() != null)
                .findFirst()
                .orElse(null);
    }

    private Promotion findPromotion(String promotionName) {
        return promotions.stream()
                .filter(p -> p.promotionName().equals(promotionName))
                .findFirst()
                .orElse(null);
    }

    boolean promotionDateValid(Promotion promotion) {
        LocalDateTime now = DateTimes.now();
        LocalDateTime start = toDate(promotion.promotionStartDate());
        LocalDateTime end = toDate(promotion.promotionEndDate());
        return !now.isBefore(start) && !now.isAfter(end);
    }

    private void applyPromotion(Product product, Promotion promotion, Order order) {
        if ("탄산2+1".equals(promotion.promotionName())) {
            twoPlusOnePromotion(product, promotion, order);
        } else if ("MD추천상품".equals(promotion.promotionName()) || "반짝할인".equals(promotion.promotionName())) {
            onePlusOnePromotion(product, promotion, order);
        }
    }

    void onePlusOnePromotion(Product product, Promotion promotion, Order order) {
        createBenefit(product, order, promotion.promotionBuy() + 1);
    }

    void twoPlusOnePromotion(Product product, Promotion promotion, Order order) {
        int remainder = order.orderQuantity() % 3;
        createBenefit(product, order, promotion.promotionBuy() + 1);

        if (remainder != 0) {
            applyPromotionIfAgreed(order, promotion, remainder, product.productPrice());
        }
    }

    private void applyPromotionIfAgreed(Order order, Promotion promotion, int remainder, int price) {
        String answer = inputView.promotionInventoryLowInput(order.orderName(), remainder);
        if ("Y".equalsIgnoreCase(answer)) {
            addBenefit(order, remainder, price);
        }
    }

    private void addBenefit(Order order, int quantity, int price) {
        List<Benefit> benefits = receipt.benefits();
        Benefit benefit = new Benefit(order.orderName(), quantity, price);
        benefits.add(benefit);
        receipt.reduceBenefitDiscount(price * quantity);
    }

    private LocalDateTime toDate(String date) {
        String[] parts = date.split("-");
        return LocalDateTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 0, 0);
    }

    private static void createBenefit(Product product, Order order, int promotionSize) {
        int quantity = order.orderQuantity() / promotionSize;
        Benefit benefit = new Benefit(order.orderName(), quantity, product.productPrice());
        receipt.addBenefit(benefit);
    }
}
