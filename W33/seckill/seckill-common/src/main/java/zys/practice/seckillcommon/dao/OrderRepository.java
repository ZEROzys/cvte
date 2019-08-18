package zys.practice.seckillcommon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zys.practice.seckillcommon.pojo.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
