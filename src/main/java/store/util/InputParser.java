package store.util;

import java.util.Arrays;

public class InputParser {

    public String[] orderParser(String input) {
        String[] commaParserResult = commaParser(input);
        String[] removeBracketsResult = removeBrackets(commaParserResult);
        return hyphenParser(removeBracketsResult);
    }


    public String[] hyphenParser(String[] orders) {
        for (int i = 0; i < orders.length; i++) {
            orders[i] = Arrays.toString(orders[i].split("-"));
        }
        return orders;
    }

    public String[] commaParser(String input) {
        return input.split(", ");
    }

    public String[] removeBrackets(String[] orders) {
        for (int i = 0; i < orders.length; i++) {
            orders[i] = orders[i].replaceAll("[\\[|\\]]", "");
        }
        return orders;
    }

}
