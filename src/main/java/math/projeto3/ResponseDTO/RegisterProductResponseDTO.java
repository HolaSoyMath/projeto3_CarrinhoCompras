package math.projeto3.ResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;

public class RegisterProductResponseDTO {

    private Long idProduct;
    private String name;
    private String description;
    private Double price;
    @Schema(type = "string", format = "byte", description = "Imagem em formato base64")
    private byte[] image;

    // Getters e Setters

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}