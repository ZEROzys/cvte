package zys.learning.miaoshaproducer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zys.learning.miaoshaproducer.dao.ProductRepository;
import zys.learning.miaoshaproducer.pojo.Product;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.getAll();
    }

}

