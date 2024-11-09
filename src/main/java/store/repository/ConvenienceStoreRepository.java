package store.repository;

import store.domain.Product;
import store.domain.Promotion;

import java.util.ArrayList;
import java.util.List;

public class ConvenienceStoreRepository {
    public static List<Product> products = new ArrayList<>();
    public final static List<Promotion> promotions = new ArrayList<>();
}
