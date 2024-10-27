package math.projeto3.RequestDTO;

public class NewItemShoppingRequestDTO {

    private Long idUser;
    private Long idProduct;
    private Integer quantity;

    //Getters e Setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduto) {
        this.idProduct = idProduto;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
