package store.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        String[] result = inputParser.orderParser(input);

        assertEquals("[콜라, 10]", result[0]);
        assertEquals("[바나나, 10]", result[1]);
    }

    @Test
    void 상품명과_수량_분리_테스트() {
        String input = "[콜라-10], [바나나-10]";
        /*inputParser.productParser(input);*/
    }

    @Test
    void 문자열_제외하기_테스트() {
        String[] input = {"[콜라-10]", "[바나나-10]"};
        String[] orders = inputParser.removeBrackets(input);
        assertEquals("콜라-10", orders[0]);
        assertEquals("바나나-10", orders[1]);

    }

    @Test
    void 하이픈_파싱_테스트() {
        String[] input = {"콜라-10", "바나나-10"};
        String[] orders = inputParser.hyphenParser(input);
        assertEquals("[콜라, 10]", orders[0]);
        assertEquals("[바나나, 10]", orders[1]);

    }

    @Test
    void 사용자에게_입력받은_상품리스트_파싱_테스트() {
        String input = "[콜라-10], [바나나-10]";
        String[] products = inputParser.commaParser(input);
        assertEquals("[콜라-10]", products[0]);
        assertEquals("[바나나-10]", products[1]);
    }

}