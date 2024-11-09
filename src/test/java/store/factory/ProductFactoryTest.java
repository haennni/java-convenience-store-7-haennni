package store.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.Product;
import store.util.FileReader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductFactoryTest {
    ProductFactory productFactory;
    FileReader fileReader;

    @BeforeEach
    void setUp() {
        productFactory = new ProductFactory();
        fileReader = new FileReader();
    }

    @Test
    void 상품_목록_리스트_생성_테스트() throws IOException {
        List<Product> products = productFactory.toProducts();
        assertEquals(products.size(), 16, "상품 갯수가 일치하지않습니다.");
        assertEquals("콜라", products.get(0).productName(), "첫 번째 상품 목록 이름이 일치하지않습니다.");
        assertEquals(1000, products.get(0).productPrice(), "첫 번째 상품의 가격이 일치하지않습니다.");
        assertEquals("비타민워터", products.get(7).productName(), "여덟 번째 상품 목록 이름이 일치하지않습니다.");
        assertEquals(8, products.get(2).productQuantity(), "세 번째 상품의 수량이 일치하지않습니다.");
        assertNull(products.get(1).productPromotionName(), "두 번째 상품의 프로모션이 일치하지않습니다.");

    }

}