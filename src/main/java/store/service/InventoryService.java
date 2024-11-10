package store.service;

import store.domain.Order;
import store.domain.Product;
import store.repository.ConvenienceStoreRepository;
import store.validation.Validation;

import java.util.List;
import java.util.Optional;

import static store.repository.ConvenienceStoreRepository.orders;
import static store.repository.ConvenienceStoreRepository.products;

public class InventoryService {

    private final PromotionService promotionService;

    public InventoryService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }


    /* 1. 재고수량과 구매 수량을 파악하여 결제 가능 여부를 파악한다.*/
    /* Products에 Poromotion.isEmpty()값이 false인 값을 추출해서, quantity를 확인해.
     * 그리고 만약에 quantity가 OrderQuantity보다 많으면 그대로 product의 수량을 차감해.
     * 만약에 quantity가 OrderQuantity보다 작아. 그러면 OutputView에서 프로모션재고 부족 결제 여부를 물어봐
     *     -> 이건 콜라 10개를 주문했는데 프로모션이 2+1이고 콜라가 7개가 존재해.
     *     -> 그럼 우선 프로모션수량 % 3 == 0이 아니면
     *     -> int 나머지 = 프로모션수량 % 3; 값을 구해서
     *     -> 주문수량 - 프로모션수량 + 나머지 값을 리턴해서 현재 프로모션 할인이 적용되지 않는 갯수가 몇개인지 말해.
     *  */
    public void checkInventory(Order order) {
        checkOrderEligibility(order, products);
    }

    public void checkOrderEligibility(Order order, List<Product> products) {
        products.stream().filter(p -> p.productName().equals(order.orderName()))
                    .forEach(p -> checkPromotionQuantity(p, order.orderQuantity()));
    }

    /*2. 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 재고에서 차감하여 수량을 관리한다.*/
    public void checkPromotionQuantity(Product product, int orderQuantity) {
        if (!(product.productPromotionName() == null) && product.productQuantity() > 0) {
            /* 프로모션이 존재하고 프로모션 수량이 0이 아니면 아래의 로직을 실행 */
            reducePromoStock(product, orderQuantity);
            return;
        }
        /* 프로모션 이름이 null이거나, 프로모션 수량이 0이라면 아래의 로직을 실행. */
        reduceRegularStock(product, orderQuantity);
    }
    /* 이름을 받아서 products에서 같은 이름인데 product의 프로모션이 null이 아닌 프로덕트를 찾아서, 리턴하고 ? reduceRegularStock을 진행*/
    private Product retrieveRegularStockProduct(String productName) {
        return products.stream()
                .filter(p -> p.productName().equals(productName))
                .filter(p -> p.productPromotionName() == null)
                .findFirst()
                .orElseThrow();
    }
    /* 3. 일반 재고 차감 로직*/
    private void reduceRegularStock(Product product, int orderQuantity) {
        product.deductionProductQuantity(orderQuantity);
    }
    /* 4. 프로모션 재고 차감 로직 (프로모션 재고 부족하면 나머지 재고를 일반 재고에서 차감)*/
    private void reducePromoStock(Product product, int orderQuantity){
        int promoQuantity = product.productQuantity(); // 프로모션 재고 수량
        if (promoQuantity >= orderQuantity) {
            product.deductionProductQuantity(orderQuantity);
        }
        /* 프로모션 적용 후 나머지 값 구하기 */
        int remainder = product.productPromotionRemainder(orderQuantity);
        product.promotionProductQuantityZero();
        Product regularProduct = retrieveRegularStockProduct(product.productName());
        reduceRegularStock(regularProduct, remainder);
    }

}
