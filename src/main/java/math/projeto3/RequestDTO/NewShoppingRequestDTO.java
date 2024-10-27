package math.projeto3.RequestDTO;

import math.projeto3.models.ShoppingProductModel;

import java.util.List;

public record NewShoppingRequestDTO(Long idUser, List<NewItemShoppingRequestDTO> shoppingProducts) {

}
