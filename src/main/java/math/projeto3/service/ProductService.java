package math.projeto3.service;

import math.projeto3.RequestDTO.NewProductRequestDTO;
import math.projeto3.ResponseDTO.NewProductResponseDTO;
import math.projeto3.models.ProductsModel;
import math.projeto3.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class ProductService {

    @Autowired
    private final ProductsRepository repository;

    public ProductService(ProductsRepository repository) {
        this.repository = repository;
    }

    // Registrar novo produto no sistema
    public ResponseEntity<NewProductResponseDTO> createProduct(String name, String description, Double price, MultipartFile imageFile) throws IOException {


        // Converter o Multipart para byte
        byte[] imageBytes = imageFile.getBytes();

        // Criar o DTO request de produto
        NewProductRequestDTO requestDTO = new NewProductRequestDTO();
        requestDTO.setName(name);
        requestDTO.setDescription(description);
        requestDTO.setPrice(price);
        requestDTO.setImage(imageBytes);

        // Verificar se já existe o produto cadastrado
        if(repository.findByName(requestDTO.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de produto já cadastrado");
        }

        // Transformar o DTO em um Model
        ProductsModel productModel = new ProductsModel();
        productModel.setName(requestDTO.getName());
        productModel.setDescription(requestDTO.getDescription());
        productModel.setPrice(requestDTO.getPrice());
        productModel.setImage(requestDTO.getImage());

        // Tentar salvar o novo produto no DB
        try {
            ProductsModel responseModel = repository.save(productModel);

            // Transformar de Model para a resposta DTO
            NewProductResponseDTO responseDTO = new NewProductResponseDTO();
            responseDTO.setIdProduct(responseModel.getIdProduct());
            responseDTO.setName(responseModel.getName());
            responseDTO.setDescription(responseModel.getDescription());
            responseDTO.setPrice(responseModel.getPrice());
            responseDTO.setImage(responseModel.getImage());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e){
            // Caso não consiga, indica que teve um erro ao salvar o usuário
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o produto.");
        }

    }

}
