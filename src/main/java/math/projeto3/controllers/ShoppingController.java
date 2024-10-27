package math.projeto3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import math.projeto3.ResponseDTO.ShoppingResponseDTO;
import math.projeto3.models.ShoppingModel;
import math.projeto3.service.ShoppingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService){
        this.shoppingService = shoppingService;
    }

    @Operation(summary = "Criar carrinho", description = "Criar um novo carrinho de compras para um usuário", tags = "Carrinho")
    @PostMapping("/criar")
    public Optional<ShoppingResponseDTO> criar(@RequestParam Long idUser){

        return shoppingService.identifyShopping(idUser);

    }

}
