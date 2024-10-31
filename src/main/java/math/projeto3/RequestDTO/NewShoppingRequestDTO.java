package math.projeto3.RequestDTO;


import java.util.List;

public record NewShoppingRequestDTO(Long idUser, List<NewItemShoppingRequestDTO> shoppingProducts) {

}
