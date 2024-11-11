package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.*;
import store.view.InputView;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static store.repository.ConvenienceStoreRepository.products;
import static store.repository.ConvenienceStoreRepository.promotions;
import static store.repository.ConvenienceStoreRepository.*;

class PromotionServiceTest {
    private PromotionService promotionService;
    private InputView inputView;

    @BeforeEach
    void setUp() {
        inputSetting();
        dataClear();
        promotionService = new PromotionService(inputView);
        Product product1 = new Product("콜라", 1000, 10, "탄산2+1");
        Product product2 = new Product("오렌지주스", 1800, 9, "MD추천상품");
        products.addAll(Arrays.asList(product1, product2));
        Promotion promo1 = new Promotion("탄산2+1", 2, 1, "2023-11-01", "2024-12-01");
        Promotion promo2 = new Promotion("MD추천상품", 1, 1, "2023-11-10", "2024-12-10");
        promotions.addAll(Arrays.asList(promo1, promo2));
    }

    void dataClear() {
        products.clear();
        receipt = new Receipt(); // 기존 receipt 객체 초기화
    }

    void inputSetting() {
        inputView = new InputView() {
            @Override
            public String promotionInventoryLowInput(String productName, int remainder) {
                return "Y";
            }

            @Override
            public String promotionFreeBenefitInput(String productName, int promotionGet) {
                return "Y";
            }
        };
    }

    @Test
    @DisplayName("1+1 프로모션이 적용될 때, 주문 수량에 따른 남은 수량을 올바르게 처리")
    void 원_플러스_원_남은_수량을_올바르게_처리하는_지_확인하는_테스트() {
        Order order = new Order("사이다", 5);
        Promotion promotion = promotions.get(1);
        Product product = products.get(1);
        promotionService.onePlusOnePromotion(product,promotion,order);
        assertEquals(6, order.orderQuantity(), "1+1 프로모션 적용 후 주문 수량이 올바르게 증가해야 함");
    }

    @Test
    @DisplayName("2+1 프로모션이 적용될 때, 주문 수량에 따른 남은 수량을 올바르게 처리")
    void 투_플러스_원_남은_수량을_올바르게_처리하는_지_확인하는_테스트() {
        Order order = new Order("콜라", 5);  // 콜라는 2+1 프로모션
        Promotion promotion = promotions.get(0);
        Product product = products.get(0);
        promotionService.twoPlusOnePromotion(product,promotion,order);
        assertEquals(6, order.orderQuantity(), "2+1 프로모션 적용 후 주문 수량이 올바르게 증가해야 함");
    }


    @Test
    @DisplayName("주문 날짜가 프로모션의 기간 범위 내에 속하는지 테스트한다.")
    void 프로모션의_유효한_날짜범위를_검사하는_테스트() {
        Promotion validPromotion = new Promotion("탄산2+1", 2, 1, "2023-11-01", "2024-12-01");
        boolean isValid = promotionService.promotionDateValid(validPromotion);
        assertTrue(isValid, "프로모션의 날짜가 유효한 범위 내에 있어야 함");
        Promotion expiredPromotion = new Promotion("MD추천상품", 1, 1, "2022-01-01", "2024-01-31");
        boolean isExpired = promotionService.promotionDateValid(expiredPromotion);
        assertThat(isExpired).isFalse();
    }
}