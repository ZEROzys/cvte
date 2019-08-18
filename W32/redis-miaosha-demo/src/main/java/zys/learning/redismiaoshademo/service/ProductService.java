package zys.learning.redismiaoshademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zys.learning.redismiaoshademo.dao.ProductRepository;
import zys.learning.redismiaoshademo.pojo.Order;
import zys.learning.redismiaoshademo.pojo.Product;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    /***
     * 保存订单，库存减一，出现错误回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Order order) throws Exception{
        Product product = productRepository.getProductById(order.getProductId());
        if (product.getStock() > 0) {
            Product updatedProduct = new Product(product.getId(), product.getStock()-1,
                    product.getProductName(), product.getPrice());
            if (productRepository.save(updatedProduct) == null)
                throw new Exception("更新库存失败");
            if (orderService.save(order) == null)
                throw new Exception("订单保存失败");
            return true;
        }
        return false;
    }

    public List<Product> getAll() {
        return productRepository.getAll();
    }
    public Product getProductById(long id) {
        return productRepository.getOne(id);
    }
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
