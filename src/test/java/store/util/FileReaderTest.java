package store.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.domain.FilePath;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    FileReader fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReader();
    }

    @Test
    void 상품_리스트_파일_불러오기_테스트() throws IOException {
        List<String> products = fileReader.readProducts();
        assertEquals(19, products.size(),"products.md 파일의 사이즈가 일치하지않습니다.");
        assertEquals("콜라,1000,10,탄산2+1", products.get(1),"index 1번 상품 리스트 목록이 일치하지않습니다.");
        assertEquals("컵라면,1700,10,null", products.get(18),"index 16번 상품 리스트 목록이 일치하지않습니다.");
    }

    @Test
    void 프로모션_리스트_파일_불러오기_테스트() throws IOException {
        List<String> promotions = fileReader.readPromotions();
        assertEquals(4, promotions.size(),"promotions.md 파일의 사이즈가 일치하지않습니다.");
        assertEquals("탄산2+1,2,1,2024-01-01,2024-12-31", promotions.get(1),"index 1번 프로모션이 목록이 일치하지않습니다.");
        assertEquals("반짝할인,1,1,2024-11-01,2024-11-30", promotions.get(3),"index 3번 프로모션이 일치하지않습니다.");
    }

}