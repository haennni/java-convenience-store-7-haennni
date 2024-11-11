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
    @DisplayName("영수증 출력이 올바르게 이루어지는지 확인")
    void testRun() {
        // 테스트 입력값 설정
        setTestInput("[콜라-2], [사이다-1]\nY\nY\nY\nN\n");

        // 테스트 데이터 설정
        repository.products().add(new Product("콜라", 1000, 5, "탄산2+1"));
        repository.products().add(new Product("사이다", 2000, 10, null));
        repository.promotions().add(new Promotion("탄산2+1", 2, 1, "2023-01-01", "2024-12-31"));

        // 주문 데이터를 로드
        repository.loadOrders(inputView, new InputParser());

        // 서비스 실행
        convenienceStoreService.run();

        assertEquals(4000, ConvenienceStoreRepository.receipt.originalAmount(), "총구매액은 4,000원이여야 합니다.");
        assertEquals(1000, ConvenienceStoreRepository.receipt.benefitDiscount(), "행사할인은 1,000원이여야 합니다.");
        assertEquals(3000, ConvenienceStoreRepository.receipt.finalAmount(), "내실돈은 3,000원이여야 합니다.");
    }
}