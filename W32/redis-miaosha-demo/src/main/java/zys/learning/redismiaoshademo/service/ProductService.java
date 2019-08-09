package zys.learning.redismiaoshademo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zys.learning.redismiaoshademo.dao.ProductRepository;
import zys.learning.redismiaoshademo.pojo.Order;
import zys.learning.redismiaoshademo.pojo.Product;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderService orderService;

    @Transactional(rollbackFor = Exception.class)
    public boolean updateStock(Long productId, Long userId) throws Exception {
        Product product = productRepository.getProductById(productId);
        if (product.getStock() > 0) {
            Product updatedProduct = new Product(product.getId(), product.getStock()-1,
                    product.getProductName(), product.getPrice());
            productRepository.save(updatedProduct);
//            while (true) {}
            Order order = new Order(productId, userId);
            if (orderService.save(order) == null)
                throw new Exception("订单保存失败");
            return true;
        }
        return false;
    }

}
