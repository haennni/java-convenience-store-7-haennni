package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.repository.ConvenienceStoreRepository;
import store.util.InputParser;
import store.view.InputView;

import java.io.IOException;

class InventoryServiceTest {
    private InputView inputView;
    private InventoryService inventoryService;
    private ConvenienceStoreRepository repository;


    @BeforeEach

    void setUp() throws IOException {
        inputView = new InputView() {
            public String orderInput() {
                return "[콜라-2], [사이다-1]";
            }
        };
        repository = new ConvenienceStoreRepository(new ProductFactory(), new PromotionFactory(), new OrderFactory(), inputView, new InputParser());
        inventoryService = new InventoryService();
    }

    @Test
    @DisplayName("재고수량과 구매 수량을 파악하여 결제 가능 여부를 계산하는 기능 테스트")
    void 재고수량_구매수량_파악_후_결제_가능_여부_테스트() {
        inventoryService.checkInventory(repository);
    }
}