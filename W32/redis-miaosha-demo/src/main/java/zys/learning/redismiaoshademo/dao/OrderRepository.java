package zys.learning.redismiaoshademo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zys.learning.redismiaoshademo.pojo.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
