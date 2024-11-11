package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.Benefit;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.validation.Error;
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
        Product product = getEligiblePromotionProduct(order);
        Promotion promotion = getPromotionForProduct(product);

        validateInventory(order, product);

        if (isPromotionDateValid(promotion)) {
            applyPromotionBasedOnType(product, promotion, order);
        }
    }

    private void validateInventory(Order order, Product product) {
        int requiredQuantity = order.orderQuantity();
        int availableQuantity = product.productQuantity();
        if (availableQuantity < requiredQuantity) {
            throw new IllegalArgumentException(Error.OVER_INVENTORY.errorMsg());
        }
    }

    private Product getEligiblePromotionProduct(Order order) {
        return products.stream()
                .filter(p -> p.productName().equals(order.orderName()))
                .filter(p -> p.productPromotionName() != null && p.productQuantity() > 0)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Error.PROMOTION_PRODUCT_NOT_FOUND.errorMsg()));
    }

    private Promotion getPromotionForProduct(Product product) {
        return promotions.stream()
                .filter(p -> p.promotionName().equals(product.productPromotionName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Error.PROMOTION_NOT_FOUND.errorMsg()));
    }

    private boolean isPromotionDateValid(Promotion promotion) {
        LocalDateTime currentDate = DateTimes.now();
        return currentDate.isAfter(toDate(promotion.promotionStartDate())) &&
                currentDate.isBefore(toDate(promotion.promotionEndDate()));
    }

    private void applyPromotionBasedOnType(Product product, Promotion promotion, Order order) {
        if ("탄산2+1".equals(promotion.promotionName())) {
            applyTwoPlusOnePromotion(product, promotion, order);
            return;
        }
        if("MD추천상품".equals(promotion.promotionName()) || "반짝할인".equals(promotion.promotionName())) {
            applyOnePlusOnePromotion(product, promotion, order);
        }
    }

    void applyOnePlusOnePromotion(Product product, Promotion promotion, Order order) {
        if (product.productQuantity() > order.orderQuantity()) {
            handleOnePlusOneRemainder(product, promotion, order);
            return;
        }
        handleOnePlusOneFullPromotion(product, promotion, order);
    }

    private void handleOnePlusOneRemainder(Product product, Promotion promotion, Order order) {
        int remainder = order.orderQuantity() % 2;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        if (remainder == 1) {
            applyPromotionWithCustomerAgreement(order, promotion, true, 1, product.productPrice());
        }
    }

    private void handleOnePlusOneFullPromotion(Product product, Promotion promotion, Order order) {
        int remainder = product.productQuantity() % 2;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        int nonPromotionQuantity = order.orderQuantity() - product.productQuantity() + remainder;
        if (nonPromotionQuantity > 0) {
            applyPromotionWithCustomerAgreement(order, promotion, true, nonPromotionQuantity, product.productPrice());
        }
    }

    void applyTwoPlusOnePromotion(Product product, Promotion promotion, Order order) {
        if (product.productQuantity() > order.orderQuantity()) {
            handleTwoPlusOneRemainder(order, product, promotion, 3);
            return;}
        handleTwoPlusOneFullPromotion(order, product, promotion, 3);
    }

    private void handleTwoPlusOneRemainder(Order order, Product product, Promotion promotion, int promotionSize) {
        int remainder = order.orderQuantity() % promotionSize;
        createBenefit(product, order, promotion.promotionBuy());
        if (remainder == 1) {
            applyPromotionWithCustomerAgreement(order, promotion, true, 1, product.productPrice());
            return;}
        if (remainder == 2) {
            applyPromotionWithCustomerAgreement(order, promotion, false, 2, product.productPrice());}
    }

    private void handleTwoPlusOneFullPromotion(Order order, Product product, Promotion promotion, int promotionSize) {
        int notBenefit = order.orderQuantity() % promotionSize;
        createBenefit(product, order, promotion.promotionBuy() + 1);
        if (notBenefit != 0) {
            int remainder = product.productPromotionRemainder(order.orderQuantity()) + notBenefit;
            applyPromotionWithCustomerAgreement(order, promotion, true, remainder, product.productPrice());
        }
    }

    private void applyPromotionWithCustomerAgreement(Order order, Promotion promotion, boolean isInventoryLow, int remainder, int price) {
        String answerBenefit;
        if (isInventoryLow) {
            answerBenefit = inputView.promotionInventoryLowInput(order.orderName(), remainder);
            checkRegularPurchase(order, answerBenefit, remainder);
            return;
        }
        answerBenefit = inputView.promotionFreeBenefitInput(order.orderName(), promotion.promotionGet());
        checkFreeBenefitEligibility(order, answerBenefit, price);

    }

    private void checkRegularPurchase(Order order, String answer, int remainder) {
        if ("N".equals(answer)) {
            order.reduceQuantity(remainder);
        }
    }

    private void checkFreeBenefitEligibility(Order order, String answer, int benefitPrice) {
        if ("Y".equals(answer)) {
            order.plusOrderQuantity();
            List<Benefit> benefits = receipt.benefits();
            benefits.stream()
                    .filter(b -> b.benefitProductName().equals(order.orderName()))
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
        int discountAmount = benefitQuantity * product.productPrice();
        Benefit benefit = new Benefit(order.orderName(), benefitQuantity, discountAmount);
        System.out.println("Benefit created: " + discountAmount);
        receipt.addBenefit(benefit);
    }

    public boolean promotionDateValid(Promotion promotion) {
        LocalDateTime orderTime = DateTimes.now();
        LocalDateTime promotionStartDate = toDate(promotion.promotionStartDate());
        LocalDateTime promotionEndDate = toDate(promotion.promotionEndDate());
        return (orderTime.isEqual(promotionStartDate) || orderTime.isAfter(promotionStartDate)) &&
                (orderTime.isEqual(promotionEndDate) || orderTime.isBefore(promotionEndDate));
    }
}