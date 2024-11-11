package store.view;

import store.domain.Benefit;
import store.domain.OrderAmount;
import store.domain.Product;
import store.domain.Receipt;
import store.factory.ProductFactory;

import java.text.DecimalFormat;
import java.util.List;

public class OutputView {
    private final DecimalFormat formatter = new DecimalFormat("#,###");

    // 환영 메시지 출력 메서드
    public void welcomeMessage(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.\n");
        for (Product product : products) {
            System.out.println(product.formattingProduct());
        }
    }

    // 영수증 출력 메서드
    public void printReceipt(Receipt receipt) {
        System.out.println("==============W 편의점================");

        // 구매 상품 내역 출력
        System.out.println("상품명\t\t수량\t금액");
        for (OrderAmount order : receipt.orderAmounts()) {
            // 각 금액을 포맷팅하여 출력
            String formattedTotalPrice = formatter.format(order.totalPrice());
            System.out.printf("%s\t\t%d\t%s\n", order.orderName(), order.totalQuantity(), formattedTotalPrice);
        }

        // 증정 상품 내역 출력
        System.out.println("=============증\t정===============");
        for (Benefit benefit : receipt.benefits()) {
            System.out.printf("%s\t\t%d\n", benefit.benefitProductName(), benefit.benefitQuantity());
        }

        // 금액 정보 출력
        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\t%s\n", receipt.finalQuantity(), formatter.format(receipt.originalAmount()));
        System.out.printf("행사할인\t\t\t-%s\n", formatter.format(receipt.benefitDiscount()));
        System.out.printf("멤버십할인\t\t\t-%s\n", formatter.format(receipt.membershipDiscount()));
        System.out.printf("내실돈%s\n", formatter.format(receipt.finalAmount()));
    }
}
