package zys.learning.redismiaoshademo.pojo;

import javax.persistence.*;

@Table(name = "products")
@Entity(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "stock")
    private int stock;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "price")
    private Double price;

    public Product() {
    }

    public Product(Long id, int stock, String productName, Double price) {
        this.id = id;
        this.stock = stock;
        this.productName = productName;
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
