package math.projeto3.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name="shopping")
@Table(name="shopping")
public class ShoppingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShopping;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser", unique = true)
    private UserModel user;

    private Double total;

    @OneToMany(mappedBy = "shopping", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ShoppingProductModel> shoppingProducts = new ArrayList<>();

    // Getters e Setter
    public Long getIdShopping() {
        return idShopping;
    }

    public void setIdShopping(Long idShopping) {
        this.idShopping = idShopping;
    }

    public UserModel getIdUser() {
        return user;
    }

    public void setIdUser(UserModel user) {
        this.user = user;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<ShoppingProductModel> getShoppingProducts() {
        return shoppingProducts;
    }

    public void setShoppingProducts(List<ShoppingProductModel> shoppingProducts) {
        this.shoppingProducts = shoppingProducts;
    }

    public void addShoppingProduct(ShoppingProductModel shoppingProduct) {
        // Setar o produto no shopping
        shoppingProduct.setShopping(this);
        this.shoppingProducts.add(shoppingProduct);
    }
}
