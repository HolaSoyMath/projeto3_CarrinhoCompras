package math.projeto3.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.List;

@Entity(name="products")
@Table(name="products")
public class ProductsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;
    private String name;
    private String description;
    private Double price;
    @Lob
    private byte[] image;

    @OneToMany(mappedBy = "product")
    private List<ShoppingProductModel> idShoppingProduct;

    // Getters e Setters

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<ShoppingProductModel> getIdShoppingProduct() {
        return idShoppingProduct;
    }

    public void setIdShoppingProduct(List<ShoppingProductModel> idShoppingProduct) {
        this.idShoppingProduct = idShoppingProduct;
    }
}
