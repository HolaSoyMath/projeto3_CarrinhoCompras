package math.projeto3.models;

import jakarta.persistence.*;

@Entity(name="shopping")
@Table(name="shopping")
public class ShoppingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShopping;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser")
    private UserModel idUser;

    private Double total;

    @OneToMany(mappedBy = "idShopping")
    private ShoppingProductModel idShoppingProduct;

    // Getters e Setter
    public Long getIdShopping() {
        return idShopping;
    }

    public void setIdShopping(Long idShopping) {
        this.idShopping = idShopping;
    }

    public UserModel getIdUser() {
        return idUser;
    }

    public void setIdUser(UserModel idUser) {
        this.idUser = idUser;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public ShoppingProductModel getIdShoppingProduct() {
        return idShoppingProduct;
    }

    public void setIdShoppingProduct(ShoppingProductModel idShoppingProduct) {
        this.idShoppingProduct = idShoppingProduct;
    }
}
