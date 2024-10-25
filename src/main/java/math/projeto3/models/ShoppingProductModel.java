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
    private ShoppingModel shopping;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private ProductsModel product;

    private Integer quantity;

    //Getters e Setters
    public Long getIdShoppingProduct() {
        return idShoppingProduct;
    }

    public void setIdShoppingProduct(Long idShoppingProduct) {
        this.idShoppingProduct = idShoppingProduct;
    }

    public ShoppingModel getShopping() {
        return shopping;
    }

    public void setShopping(ShoppingModel shopping) {
        this.shopping = shopping;
    }

    public ProductsModel getProduct() {
        return product;
    }

    public void setProduct(ProductsModel product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
