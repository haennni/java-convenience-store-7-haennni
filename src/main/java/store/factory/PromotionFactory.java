package store.factory;

import store.domain.Promotion;
import store.util.FileReader;
import store.util.InputParser;

import java.io.IOException;
import java.util.List;

import static store.repository.ConvenienceStoreRepository.promotions;

public class PromotionFactory {
    FileReader fileReader = new FileReader();
    InputParser inputParser = new InputParser();

    public List<Promotion> toPromotions() throws IOException {
        List<String> readPromotions = fileReader.readPromotions();
        for(int i = 1; i < readPromotions.size(); i++) {
            String readPromotionLine = readPromotions.get(i);
            String[] splitPromotion = inputParser.commaParser(readPromotionLine);
            Promotion promotion = toPromotion(splitPromotion);
            promotions.add(promotion);}
        return promotions;
    }

    private Promotion toPromotion(String[] promotion) {
        return new Promotion(
                promotion[0],
                Integer.parseInt(promotion[1]),
                Integer.parseInt(promotion[2]),
                promotion[3],
                promotion[4]);}
}
