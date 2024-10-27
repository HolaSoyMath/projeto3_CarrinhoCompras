package math.projeto3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import math.projeto3.RequestDTO.NewProductRequestDTO;
import math.projeto3.ResponseDTO.NewProductResponseDTO;
import math.projeto3.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @Operation(summary = "Criar produto", description = "Registrar novo produto no sistema",tags = "Produto")
    @PostMapping(value = "/create")
    public ResponseEntity<NewProductResponseDTO> criar(@RequestBody NewProductRequestDTO newProductRequestDTO)
            throws IOException {

        return productService.createProduct(newProductRequestDTO);
    }

    @GetMapping()
    public ResponseEntity<List<NewProductResponseDTO>> getAll() {

       return productService.getAll();
    }

}
