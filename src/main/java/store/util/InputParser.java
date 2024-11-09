package store.util;

import java.util.ArrayList;
import java.util.List;

public class InputParser {

    public List<String[]> orderParser(String input) {
        String[] commaParserResult = commaParser(input);
        String[] removeBracketsResult = removeBrackets(commaParserResult);
        return hyphenParser(removeBracketsResult);
    }


    private List<String[]> hyphenParser(String[] orders) {
        List<String[]> order = new ArrayList<>();
        for(int i = 0; i < orders.length; i++) {
            String[] split = orders[i].split("-");
            order.add(split);
        }
        return order;
    }

    private String[] commaParser(String input) {
        return input.split(", ");
    }

    private String[] removeBrackets(String[] orders) {
        for (int i = 0; i < orders.length; i++) {
            orders[i] = orders[i].replaceAll("[\\[|\\]]", "");}
        return orders;
    }

}
