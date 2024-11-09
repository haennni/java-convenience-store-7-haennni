package store.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputParserTest {

    InputParser inputParser;

    @BeforeEach
    void setUp() {
        inputParser = new InputParser();
    }

    @Test
    void 주문리스트_파싱_메인_테스트() {
        String input = "[콜라-10], [바나나-10]";
        List<String[]> orders = inputParser.orderParser(input);
        assertEquals("[콜라, 10]", Arrays.toString(orders.get(0)));
        assertEquals("[바나나, 10]", Arrays.toString(orders.get(1)));
    }

    @Test
    void 콤마로_문자열_파싱_테스트() {
        String input = "1+1,상품,프로모션";
        String[] result = inputParser.commaParser(input);
        assertEquals("1+1", result[0]);
        assertEquals("상품", result[1]);
        assertEquals("프로모션", result[2]);

    }
}