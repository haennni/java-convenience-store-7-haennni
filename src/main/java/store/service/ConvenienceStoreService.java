package store.service;

import store.domain.Order;
import store.domain.OrderAmount;
import store.domain.Product;
import store.repository.ConvenienceStoreRepository;
import store.validation.Error;
import store.view.InputView;
import store.view.OutputView;

import java.util.Optional;

import static store.repository.ConvenienceStoreRepository.*;

public class ConvenienceStoreService {
    private final ConvenienceStoreRepository repository;
    private final InventoryService inventoryService;
    private final PromotionService promotionService;
    private final MembershipService membershipService; // MembershipService 인스턴스 추가
    private final OutputView outputView;
    private final InputView inputView; // InputView 인스턴스 추가

    public ConvenienceStoreService(ConvenienceStoreRepository repository, InventoryService inventoryService,
                                   PromotionService promotionService, MembershipService membershipService,
                                   OutputView outputView, InputView inputView) {
        this.repository = repository;
        this.inventoryService = inventoryService;
        this.promotionService = promotionService;
        this.membershipService = membershipService; // 생성자에서 초기화
        this.outputView = outputView;
        this.inputView = inputView; // 생성자에서 초기화
    }

    public void run() {
        for (Order order : orders) {
            Optional<Product> optionalProduct = findProductByName(order.orderName());
            if (optionalProduct.isEmpty())
                throw new IllegalArgumentException(Error.PRODUCT_NOT_FOUND.errorMsg());
            Product product = optionalProduct.get();
            processOrder(order, product);
            calculateTotalOrderAmount(order);}
        receipt.calculateFinalAmount();
        applyMembershipDiscountIfRequested();
    }

    private Optional<Product> findProductByName(String orderName) {
        return products.stream()
                .filter(p -> p.productName().equals(orderName))
                .findFirst();
    }

    private void processOrder(Order order, Product product) {
        inventoryService.checkInventory(order);

        if (product.productPromotionName() == null) {
            inventoryService.checkInventory(order);
            return;
        }
        if (product.productQuantity() > 0) {
            promotionService.checkPromotion(order);
            return;
        }
    }
    private int productPrice(String orderName) {
        return findProductByName(orderName)
                .map(Product::productPrice)
                .orElse(0);
    }
    public int calculateTotalOrderAmount(Order order) {
        OrderAmount orderAmount = new OrderAmount(order.orderName(), order.orderQuantity(), productPrice(order.orderName()));
        receipt.addOrderAmount(orderAmount);
        return receipt.originalAmount();
    }
    private void applyMembershipDiscountIfRequested() {
        String answer = inputView.membershipDiscountInput();
        if (answer.equalsIgnoreCase("Y")) { // 사용자가 Y를 입력했는지 확인
            int discount = membershipService.calculateMembershipDiscount(receipt); // 멤버십 할인 계산
            System.out.println("멤버십 할인 적용 금액: " + discount + "원");
        }
    }
}
