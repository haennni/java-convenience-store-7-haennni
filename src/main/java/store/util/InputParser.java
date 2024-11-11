package store.util;

import store.validation.Error;

import java.util.ArrayList;
import java.util.List;

public class InputParser {
    public static List<String[]> orderParser(String input) {
        // 입력이 비어있지 않은지 검증
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 입력이 비어있습니다. 다시 입력해 주세요.");
        }

        String[] commaParserResult = commaParser(input);
        validateEachOrderFormat(commaParserResult); // 각 주문 항목 형식 검증
        String[] removeBracketsResult = removeBrackets(commaParserResult);
        return hyphenParser(removeBracketsResult);
    }

    // 각 주문 항목 형식 검증 메서드
    private static void validateEachOrderFormat(String[] orders) {
        for (String order : orders) {
            // [ ]로 감싸져 있는지 검증
            if (!order.startsWith("[") || !order.endsWith("]")) {
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }

            // 대괄호 제거 후 내용물 검증
            String content = order.substring(1, order.length() - 1);
            String[] parts = content.split("-");

            // 하이픈으로 나뉘어 있고, 두 개의 부분으로 구성되어 있는지 확인
            if (parts.length != 2) {
                throw new IllegalArgumentException("[ERROR] 상품명과 수량 형식이 올바르지 않습니다. 다시 입력해 주세요.");
            }

            // 첫 번째 부분이 문자로만 구성되어 있는지 확인 (상품명)
            if (!parts[0].matches("[\\w가-힣]+")) {
                throw new IllegalArgumentException("[ERROR] 상품명이 올바르지 않습니다. 다시 입력해 주세요.");
            }

            // 두 번째 부분이 숫자로만 구성되어 있는지 확인 (수량)
            if (!parts[1].matches("\\d+")) {
                throw new IllegalArgumentException("[ERROR] 수량이 올바르지 않습니다. 다시 입력해 주세요.");
            }
        }
    }

    public static String[] commaParser(String input) {
        String[] result = input.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }

    private static List<String[]> hyphenParser(String[] orders) {
        List<String[]> orderList = new ArrayList<>();
        for (String order : orders) {
            String[] split = order.split("-");
            orderList.add(split);
        }
        return orderList;
    }

    private static String[] removeBrackets(String[] orders) {
        for (int i = 0; i < orders.length; i++) {
            orders[i] = orders[i].replaceAll("[\\[\\]]", "");
        }
        return orders;
    }
}