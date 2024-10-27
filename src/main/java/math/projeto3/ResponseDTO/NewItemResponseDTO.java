package math.projeto3.ResponseDTO;

import math.projeto3.models.ProductsModel;

import java.util.List;

public class NewItemResponseDTO {

    private Long idUser;
    private Long idShopping;
    private Long idSHoppingProduct;
    private List<ProductsModel> itens;
    private Double total;

    // Getters e Setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdShopping() {
        return idShopping;
    }

    public void setIdShopping(Long idShopping) {
        this.idShopping = idShopping;
    }

    public Long getIdSHoppingProduct() {
        return idSHoppingProduct;
    }

    public void setIdSHoppingProduct(Long idSHoppingProduct) {
        this.idSHoppingProduct = idSHoppingProduct;
    }

    public List<ProductsModel> getItens() {
        return itens;
    }

    public void setItens(List<ProductsModel> itens) {
        this.itens = itens;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}