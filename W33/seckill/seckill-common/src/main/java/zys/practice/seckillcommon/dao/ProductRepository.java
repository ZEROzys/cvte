package zys.practice.seckillcommon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import zys.practice.seckillcommon.pojo.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select id, product_name, stock, price from products where id = :id for update", nativeQuery = true)
    Product getProductById(@Param("id") long id);

    @Query(value = "select id, product_name, stock, price from products limit 10", nativeQuery = true)
    List<Product> getAll();
}
