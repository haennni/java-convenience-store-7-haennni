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

}