package math.projeto3.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ShoppingResponseDTO(Long idShopping,
                                  Long idUser,
                                  Double total,
                                  List<ShoppingProductDTO> products) {
}
