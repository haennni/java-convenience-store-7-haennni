package store.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Order;
import store.domain.Product;
import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.repository.ConvenienceStoreRepository;
import store.util.InputParser;
import store.validation.Error;
import store.view.InputView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static store.repository.ConvenienceStoreRepository.products;

class InventoryServiceTest {
    private InputView inputView;
    private InventoryService inventoryService;
    private PromotionService promotionService;
    private List<Product> testProducts = new ArrayList<>();

    @BeforeEach
    void setUp() throws IOException {
        InputView.enableTestMode();
        inputView = new InputView();
        new ConvenienceStoreRepository(new ProductFactory(), new PromotionFactory(), new OrderFactory(), new InputParser());
        addProducts();
        products.clear();
        products.addAll(testProducts);
        promotionService = new PromotionService(inputView);
        inventoryService = new InventoryService(promotionService);
    }

    // 사용자 입력 설정 메서드
    private void setTestInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    void addProducts() {
        testProducts.add(new Product("초코바", 1200, 10, "MD추천상품"));
        testProducts.add(new Product("초코바", 1200, 10, null));
        testProducts.add(new Product("사이다", 1000, 10, "탄산2+1"));
        testProducts.add(new Product("사이다", 1000, 10, null));
        testProducts.add(new Product("물", 500, 10, null));
    }

    @Test
    @DisplayName("일반 재고만 있는 경우 정상적으로 재고를 차감")
    void 프로모션_없는_상품_재고_차감_테스트() {
        setTestInput("N\n");
        Order order = new Order("물", 5);
        inventoryService.checkInventory(order);
        assertEquals(5, testProducts.get(4).productQuantity(), "[물] 재고 차감 실패");
    }

    @Test
    @DisplayName("재고 수량과 구매 수량을 파악하여 결제 가능 여부를 계산한다.")
    void 재고수량_구매수량_파악_후_결제_가능_여부_테스트() {
        setTestInput("Y\n"); // 사용자가 일반 재고로 구매에 동의

        Order order = new Order("사이다", 3);

        assertDoesNotThrow(() -> inventoryService.checkInventory(order));
    }
    @AfterEach
    void tearDown() {
        InputView.disableTestMode();
    }
}