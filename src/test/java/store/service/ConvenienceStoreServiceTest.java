package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Order;
import store.domain.Product;
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

import static org.junit.jupiter.api.Assertions.*;
import static store.repository.ConvenienceStoreRepository.receipt;

class ConvenienceStoreServiceTest {
    private ConvenienceStoreRepository repository;
    private InventoryService inventoryService;
    private PromotionService promotionService;
    private OutputView outputView;
    private ConvenienceStoreService convenienceStoreService;
    private InputView inputView;
    private MembershipService membershipService;

    @BeforeEach
    void setUp() throws IOException {
        ProductFactory productFactory = new ProductFactory();
        PromotionFactory promotionFactory = new PromotionFactory();
        OrderFactory orderFactory = new OrderFactory();
        InputParser inputParser = new InputParser();

        // InputView에 사용자 입력 대신 고정된 값을 사용하도록 수정
        inputView = new InputView() {
            @Override
            public String membershipDiscountInput() {
                return "N";
            }
        };

        receipt = new Receipt();
        repository = new ConvenienceStoreRepository(productFactory, promotionFactory, orderFactory, inputParser);
        promotionService = new PromotionService(inputView);
        inventoryService = new InventoryService(promotionService);
        membershipService = new MembershipService();
        outputView = new OutputView();
        convenienceStoreService = new ConvenienceStoreService(repository, inventoryService, promotionService, membershipService, outputView, inputView);
    }
}