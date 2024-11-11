package store.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.Order;
import store.domain.Product;
import store.domain.Promotion;
import store.factory.OrderFactory;
import store.factory.ProductFactory;
import store.factory.PromotionFactory;
import store.util.InputParser;
import store.view.InputView;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ConvenienceStoreRepositoryTest {

    ConvenienceStoreRepository repository;
    ProductFactory productFactory;
    PromotionFactory promotionFactory;
    OrderFactory orderFactory;
    InputView inputView;
    InputParser inputParser;

    @BeforeEach
    void setUp() throws IOException {
        productFactory = new ProductFactory();
        promotionFactory = new PromotionFactory();
        orderFactory = new OrderFactory();
        inputParser = new InputParser();
        inputView = new InputView() {public String orderInput() {return "[콜라-2], [사이다-1]";}
        };
        repository = new ConvenienceStoreRepository(productFactory, promotionFactory, orderFactory, inputParser);
    }

    @Test
    @DisplayName("저장소에서 제품 리스트를 가져오는 테스트")
    void 저장소에서_제품_리스트_가져오기_테스트() {
        List<Product> products = repository.products();
        assertNotNull(products, "제품 리스트는 null이 아니어야 합니다.");
        assertTrue(products.size() > 0, "제품 리스트는 비어있지 않아야 합니다.");
        assertEquals("콜라", products.get(0).productName(),"주문 리스트의 첫 번째 목록이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("저장소에서 주문 리스트를 가져오는 테스트")
    void 저장소에서_주문_리스트_가져오기_테스트() {
        // loadOrders 호출을 통해 주문 데이터를 초기화
        repository.loadOrders(inputView, inputParser);

        List<Order> orders = repository.orders();
        assertNotNull(orders, "주문 리스트는 null이 아니어야 합니다.");
        assertTrue(orders.size() > 0, "주문 리스트는 비어있지 않아야 합니다.");
        assertEquals("콜라", orders.get(0).orderName(),"첫 번째 주문의 제품 이름이 일치하지 않습니다.");
        assertEquals("사이다", orders.get(1).orderName(),"두 번째 주문의 제품 이름이 일치하지 않습니다.");
        assertEquals(2, orders.get(0).orderQuantity(),"첫 번째 주문 수량이 일치하지 않습니다.");
    }


    @Test
    @DisplayName("저장소에서 프로모션 리스트를 가져오는 테스트")
    void 저장소에서_프로모션_리스트_가져오기_테스트() {
        List<Promotion> promotions = repository.promotions();
        assertEquals("탄산2+1", promotions.get(0).promotionName(),"첫 번째 프로모션 이름이 일치하지 않습니다.");
        assertEquals(1, promotions.get(2).promotionGet(),"첫 번째 프로모션 제공 상품 수량이 일치하지 않습니다.");
    }
}