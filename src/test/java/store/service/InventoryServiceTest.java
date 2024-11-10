package store.service;

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
import store.view.InputView;

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
        inputView = new InputView() {
            public String orderInput() {
                return "[초코바-12], [사이다-1]";
            }
        };
        new ConvenienceStoreRepository(new ProductFactory(), new PromotionFactory(), new OrderFactory(), inputView, new InputParser());
        addProducts();
        products.clear();
        products.addAll(testProducts);
        promotionService = new PromotionService(new InputView());
        inventoryService = new InventoryService(promotionService);
    }

    void addProducts() {
        testProducts.add(new Product("초코바", 1200, 10, "MD추천상품"));
        testProducts.add(new Product("초코바", 1200, 10, null));
        testProducts.add(new Product("사이다", 1000, 10, "탄산2+1"));
        testProducts.add(new Product("사이다", 1000, 10, null));
        testProducts.add(new Product("물", 500, 10, null));
    }

    @Test
    @DisplayName("프로모션 재고가 존재하면 프로모션 수량을 차감하고 프로모션 재고가 없다면 일반 프로모션 재고를 차감한다." +
            "만약 프로모션 재고보다 주문 수량이 크다면, 나머지 주문 수량 값은 일반 재고에서 차감한다.")
    void 상품_재고_차감_테스트() {
        inventoryService.checkPromotionQuantity(testProducts.get(0), 12);
        assertSame(products.get(0), testProducts.get(0), "products 리스트와 testProducts 리스트의 첫 번째 요소가 동일한 객체여야 합니다.");
        assertEquals(0, testProducts.get(0).productQuantity(), "[초코바]프로모션 수량 차감에 실패하였습니다.");
        assertEquals(8, testProducts.get(1).productQuantity(), "[초코바]일반재고 수량 차감에 실패하였습니다.");

    }


    @Test
    @DisplayName("재고수량과 구매 수량을 파악하여 결제 가능 여부를 계산한다.")
    void 재고수량_구매수량_파악_후_결제_가능_여부_테스트() {
        inventoryService.checkInventory(new Order("콜라", 3));
    }
}