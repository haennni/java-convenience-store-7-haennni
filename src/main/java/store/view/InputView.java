package store.view;

import camp.nextstep.edu.missionutils.Console;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.util.InputParser.orderParser;

public class InputView {
    private static boolean isTestMode = false;
    public static void enableTestMode() {
        isTestMode = true;
    }
    public static void disableTestMode() {
        isTestMode = false;
    }
    public String orderInput() {
        while (true) {
            try {
                System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
                String input = Console.readLine();
                orderParser(input);  // 입력 형식 검증
                return input;  // 입력이 올바르면 반환하고 메서드를 종료합니다.
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }
        }
    }
    public String promotionFreeBenefitInput(String productName, int quantity) {
        System.out.println("현재 " + productName +"은(는) "+ quantity +"개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public static String promotionInventoryLowInput(String productName, int remainingQuantity) {
        if (isTestMode) {
            // 테스트 모드일 때는 기본적으로 "N" 반환
            return "Y";
        }
        // 실제 사용자 입력 받기
        System.out.println("현재 " + productName + " " + remainingQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public String membershipDiscountInput() {
        if (isTestMode) {
            return "N";
        }
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public String additionalPurchasesInput() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return Console.readLine();
    }
}
