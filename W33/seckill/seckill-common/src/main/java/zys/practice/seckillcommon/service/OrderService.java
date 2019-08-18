package zys.practice.seckillcommon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zys.practice.seckillcommon.dao.OrderRepository;
import zys.practice.seckillcommon.pojo.Order;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
