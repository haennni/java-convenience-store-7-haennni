package store.factory;

import store.domain.Order;

import java.util.List;

import static store.repository.ConvenienceStoreRepository.orders;

public class OrderFactory {
    public List<Order> toOrders(List<String[]> input){
        for(int i = 0; i < input.size(); i++) {
            String[] strings = input.get(i);
            orders.add(toOrder(strings));
        }
        return orders;
    }

    public Order toOrder(String[] input) {
        return new Order(input[0], Integer.parseInt(input[1]));
    }
}
