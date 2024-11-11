package store;

import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.repository.ConvenienceStoreRepository;
import store.service.ConvenienceStoreService;
import store.service.InventoryService;
import store.service.MembershipService;
import store.service.PromotionService;
import store.util.InputParser;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;

import static store.repository.ConvenienceStoreRepository.orders;
import static store.repository.ConvenienceStoreRepository.receipt;

public class Application {
    public static void main(String[] args) {
        try {
            ServiceContainer services = initializeServices();
            String additionalPurchase;
            do {
                services.outputView.welcomeMessage(services.repository.products());
                additionalPurchase = start(services);
            } while (additionalPurchase.equalsIgnoreCase("Y"));

        } catch (IOException e) {
            System.out.println("프로그램 실행 중 오류가 발생했습니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    private static String start(ServiceContainer services) {
        services.repository.resetReceipt();
        String additionalPurchase;
        services.repository.loadOrders(services.inputView, services.inputParser);
        services.convenienceStoreService.run();
        services.outputView.printReceipt(receipt);
        additionalPurchase = services.inputView.additionalPurchasesInput();
        orders.clear();
        return additionalPurchase;
    }

    private static ServiceContainer initializeServices() throws IOException {
        ProductFactory productFactory = new ProductFactory();
        PromotionFactory promotionFactory = new PromotionFactory();
        OrderFactory orderFactory = new OrderFactory();
        InputParser inputParser = new InputParser();
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        ConvenienceStoreRepository repository = new ConvenienceStoreRepository(productFactory, promotionFactory, orderFactory, inputParser);
        PromotionService promotionService = new PromotionService(inputView);
        InventoryService inventoryService = new InventoryService(promotionService);
        MembershipService membershipService = new MembershipService();
        ConvenienceStoreService convenienceStoreService = new ConvenienceStoreService(repository, inventoryService, promotionService, membershipService ,outputView,inputView);
        return new ServiceContainer(repository, inputView, inputParser, outputView, promotionService, inventoryService, convenienceStoreService);
    }

    // 서비스 객체들을 한 곳에 모은 컨테이너 클래스
    private static class ServiceContainer {
        final ConvenienceStoreRepository repository;
        final InputView inputView;
        final InputParser inputParser;
        final OutputView outputView;
        final PromotionService promotionService;
        final InventoryService inventoryService;
        final ConvenienceStoreService convenienceStoreService;
        ServiceContainer(ConvenienceStoreRepository repository, InputView inputView, InputParser inputParser, OutputView outputView,
                         PromotionService promotionService, InventoryService inventoryService,
                         ConvenienceStoreService convenienceStoreService) {
            this.repository = repository;
            this.inputView = inputView;
            this.inputParser = inputParser;
            this.outputView = outputView;
            this.promotionService = promotionService;
            this.inventoryService = inventoryService;
            this.convenienceStoreService = convenienceStoreService;
        }
    }
}