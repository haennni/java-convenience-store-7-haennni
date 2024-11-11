package store.factory;

import store.domain.Order;

import java.util.ArrayList;
import java.util.List;

import static store.repository.ConvenienceStoreRepository.orders;

public class OrderFactory {
    public static List<Order> toOrders(List<String[]> input){
        List<Order> orderList = new ArrayList<>();
        for (String[] strings : input) {
            orderList.add(toOrder(strings));
        }
        return orderList;
    }

    public static Order toOrder(String[] input) {
        return new Order(input[0], Integer.parseInt(input[1]));
    }
}
