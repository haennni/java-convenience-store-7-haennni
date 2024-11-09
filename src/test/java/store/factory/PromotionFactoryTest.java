package store.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Promotion;
import store.util.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static store.repository.ConvenienceStoreRepository.promotions;

class PromotionFactoryTest {
    PromotionFactory promotionFactory;

    @BeforeEach
    void setUp() {
        promotionFactory = new PromotionFactory();
        promotions.clear();
    }

    @Test
    void 프로모션_목록_리스트_생성_테스트() throws IOException {
        promotionFactory.toPromotions();
        assertEquals(promotions.size(), 3, "프로모션 갯수가 일치하지않습니다.");
        assertEquals("탄산2+1", promotions.get(0).promotionName(), "[탄산2+1]프로모션 이름이 일치하지 않습니다.");
        assertEquals(2, promotions.get(0).promotionBuy(), "[탄산2+1]프로모션 적용 갯수가 일치하지 않습니다.");
        assertEquals("반짝할인", promotions.get(2).promotionName(), "[반짝할인]프로모션 이름이 일치하지 않습니다.");
        assertEquals("2024-11-30", promotions.get(2).promotionEndDate(), "[반짝할인]프로모션 끝나는 기간이 일치하지 않습니다.");
    }
}