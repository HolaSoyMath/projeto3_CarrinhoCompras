package math.projeto3.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import math.projeto3.models.ShoppingProductModel;

import java.util.List;

public class ShoppingResponseDTO {

    @Schema(description = "ID do carrinho", example = "Long")
    private Long idShopping;

    @Schema(description = "ID do usuário", example = "Long")
    private Long idUser;

    @Schema(description = "Total do carrinho", example = "Double")
    private Double total;

    @Schema(description = "Produtos no carrinho", example = "[]")
    private List<ShoppingProductModel> products;

    //Getter e Setter
    public Long getIdShopping() {
        return idShopping;
    }

    public void setIdShopping(Long idShopping) {
        this.idShopping = idShopping;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<ShoppingProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ShoppingProductModel> products) {
        this.products = products;
    }
}
