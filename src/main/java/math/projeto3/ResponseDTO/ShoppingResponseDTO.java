package math.projeto3.ResponseDTO;

import java.util.List;

public record ShoppingResponseDTO(Long idShopping,
                                  Long idUser,
                                  Double total,
                                  List<ShoppingProductDTO> products) {
}
