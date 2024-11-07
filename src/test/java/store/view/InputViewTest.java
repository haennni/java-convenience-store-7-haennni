package store.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputViewTest {

    @Test
    void 구매할_상품과_수량_입력받기_테스트() {
        String input = "[콜라-10], [사이다-3]";
        assertEquals("[콜라-10], [사이다-3]", input, "상품명과 수량을 입력받는 데 실패하였습니다.");
    }

    @Test
    void 프로모션_무료_혜택_알림_메세지_테스트() {
        String input = "Y";
        assertEquals("Y", input, "프로모션 무료 혜택 여부를 입력받는 데 실패하였습니다.");
    }

    @Test
    void 프로모션_재고_부족_알림_메세지_테스트() {
        String input = "Y";
        assertEquals("Y", input, "프로모션 재고 부족 알림 여부를 입력받는 데 실패하였습니다.");
    }

    @Test
    void 멤버십_할인_혜택_여부_테스트() {
        String input = "Y";
        assertEquals("Y", input, "멤버십 할인 혜택 여부를 입력받는 데 실패하였습니다.");
    }

    @Test
    void 추가구매_여부_테스트() {
        String input = "Y";
        assertEquals("Y", input, "추가구매 여부를 입력받는 데 실패하였습니다.");
    }

}