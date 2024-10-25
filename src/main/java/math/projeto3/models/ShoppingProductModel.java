package math.projeto3.models;

import jakarta.persistence.*;

@Entity(name="shoppingProduct")
@Table(name="shoppingProduct")
public class ShoppingProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShoppingProduct;

    @ManyToOne
    @JoinColumn(name = "idShopping")
    private ShoppingModel idShopping;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private ProductsModel idProduct;

    private Double quantity;

    //Getters e Setters
    public Long getIdShoppingProduct() {
        return idShoppingProduct;
    }

    public void setIdShoppingProduct(Long idShoppingProduct) {
        this.idShoppingProduct = idShoppingProduct;
    }

    public ShoppingModel getIdShopping() {
        return idShopping;
    }

    public void setIdShopping(ShoppingModel idShopping) {
        this.idShopping = idShopping;
    }

    public ProductsModel getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(ProductsModel idProduct) {
        this.idProduct = idProduct;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
