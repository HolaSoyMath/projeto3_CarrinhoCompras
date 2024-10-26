package math.projeto3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import math.projeto3.RequestDTO.RegisterProductRequestDTO;
import math.projeto3.ResponseDTO.RegisterProductResponseDTO;
import math.projeto3.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @Operation(summary = "Criar produto", description = "Registrar novo produto no sistema",tags = "Produto")
    @PostMapping(value = "/create")
    public ResponseEntity<RegisterProductResponseDTO> criar(@RequestParam("name") String name,
                                                            @RequestParam("description") String description,
                                                            @RequestParam("price") Double price,
                                                            @RequestParam("image") MultipartFile imageFile) throws IOException {

        // Criar o produto
        RegisterProductResponseDTO responseDTO = productService.createProduct(name, description, price, imageFile).getBody();

        return ResponseEntity.ok(responseDTO);

    }

}
