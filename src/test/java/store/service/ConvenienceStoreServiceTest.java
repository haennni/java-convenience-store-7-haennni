package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Receipt;
import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.repository.ConvenienceStoreRepository;
import store.util.InputParser;
import store.validation.Error;
import store.view.InputView;
import store.view.OutputView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConvenienceStoreServiceTest {
    private ConvenienceStoreRepository repository;
    private InventoryService inventoryService;
    private PromotionService promotionService;
    private OutputView outputView;
    private ConvenienceStoreService convenienceStoreService;
    private InputView inputView;

    @BeforeEach
    void setUp() throws IOException {
        ProductFactory productFactory = new ProductFactory();
        PromotionFactory promotionFactory = new PromotionFactory();
        OrderFactory orderFactory = new OrderFactory();
        InputParser inputParser = new InputParser();
        inputView = new InputView();
        ConvenienceStoreRepository.receipt = new Receipt();
        repository = new ConvenienceStoreRepository(productFactory, promotionFactory, orderFactory, inputParser);
        promotionService = new PromotionService(inputView);
        inventoryService = new InventoryService(promotionService);
        outputView = new OutputView();
        convenienceStoreService = new ConvenienceStoreService(repository, inventoryService, promotionService, outputView);
    }

    private void setTestInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    @DisplayName("재고가 없거나 주문 수량이 더 많을 경우 예외 발생")
    void testOutOfStockOrExceedsInventory() {
        repository.products().add(new Product("콜라", 1000, 0, null));
        repository.orders().add(new Order("콜라", 1));

        // 예외 검증
        Exception exception = assertThrows(IllegalArgumentException.class, () -> convenienceStoreService.run());
        assertEquals(Error.OVER_INVENTORY.errorMsg(), exception.getMessage());
    }

}