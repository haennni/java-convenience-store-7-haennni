package store.factory;

import store.domain.Product;
import store.util.FileReader;
import store.util.InputParser;

import java.io.IOException;
import java.util.List;

import static store.repository.ConvenienceStoreRepository.products;


public class ProductFactory {
    FileReader fileReader = new FileReader();
    InputParser inputParser = new InputParser();

    public List<Product> toProducts() throws IOException {
        List<String> readProducts = fileReader.readProducts();
        for(int i = 1; i < readProducts.size(); i++) {
            String readProductLine = readProducts.get(i);
            String[] splitProduct = inputParser.commaParser(readProductLine);
            Product product = toProduct(splitProduct);
            products.add(product);}
        return products;
    }
    private Product toProduct(String[] product) {
        return new Product(
                product[0],
                Integer.parseInt(product[1]),
                Integer.parseInt(product[2]),
                checkPromotion(product[3]));
    }
    private String checkPromotion(String promotionName) {
        if (!(promotionName.equals("null"))) {
            return promotionName;
        }
        return null;
    }
}
