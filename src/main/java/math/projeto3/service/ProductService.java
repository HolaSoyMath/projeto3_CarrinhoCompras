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
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private final ProductsRepository repository;

    public ProductService(ProductsRepository repository) {
        this.repository = repository;
    }

    // Registrar novo produto no sistema
    public ResponseEntity<NewProductResponseDTO> createProduct(NewProductRequestDTO newProductRequestDTO)
            throws IOException {

        // Verificar se já existe o produto cadastrado
        if(repository.findByName(newProductRequestDTO.name()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de produto já cadastrado");
        }

        // Transformar o DTO em um Model
        ProductsModel productModel = new ProductsModel();
        productModel.setName(newProductRequestDTO.name());
        productModel.setDescription(newProductRequestDTO.description());
        productModel.setPrice(newProductRequestDTO.price());
        if (newProductRequestDTO.image() != null) {
            productModel.setImage(newProductRequestDTO.image());
        }

        // Tentar salvar o novo produto no DB
        try {
            ProductsModel responseModel = repository.save(productModel);

            // Transformar de Model para a resposta DTO
            NewProductResponseDTO newProductResponseDTO = new NewProductResponseDTO();
            newProductResponseDTO.setIdProduct(responseModel.getIdProduct());
            newProductResponseDTO.setName(responseModel.getName());
            newProductResponseDTO.setDescription(responseModel.getDescription());
            newProductResponseDTO.setPrice(responseModel.getPrice());
            newProductResponseDTO.setImage(responseModel.getImage());

            return ResponseEntity.ok().body(newProductResponseDTO);
        } catch (Exception e){
            // Caso não consiga, indica que teve um erro ao salvar o usuário
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o produto.");
        }

    }

    public ResponseEntity<List<NewProductResponseDTO>> getAll() {
        List<ProductsModel> productsModels = repository.findAll();
        List<NewProductResponseDTO> productResponseDTOS = new ArrayList<>();
        for (ProductsModel productsModel : productsModels) {
            NewProductResponseDTO newProductResponseDTO = new NewProductResponseDTO();
            newProductResponseDTO.setIdProduct(productsModel.getIdProduct());
            newProductResponseDTO.setName(productsModel.getName());
            newProductResponseDTO.setDescription(productsModel.getDescription());
            newProductResponseDTO.setPrice(productsModel.getPrice());
            newProductResponseDTO.setImage(productsModel.getImage());
            productResponseDTOS.add(newProductResponseDTO);
        }
        return ResponseEntity.ok().body(productResponseDTOS);
    }

}
