package math.projeto3.models;

import jakarta.persistence.*;

@Entity(name="shoppingProduct")
@Table(name="shoppingProduct")
public class ShoppingProductModel {

    @Id

    @Id
    @OneToOne
    private ShoppingModel idShopping;

    @Id
    @OneToOne
    private ProductsModel idProduct;

    private Double quantity;

}
