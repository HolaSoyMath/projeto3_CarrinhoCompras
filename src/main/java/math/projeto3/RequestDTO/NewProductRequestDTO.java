package math.projeto3.RequestDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public record NewProductRequestDTO(String name,
                                   String description,
                                   Double price,
                                   @Schema(type = "string", format = "byte", description = "Imagem em formato base64")
                                   MultipartFile image) {
}
