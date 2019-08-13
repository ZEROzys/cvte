package zys.learning.redismiaoshademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zys.learning.redismiaoshademo.dao.OrderRepository;
import zys.learning.redismiaoshademo.pojo.Order;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
