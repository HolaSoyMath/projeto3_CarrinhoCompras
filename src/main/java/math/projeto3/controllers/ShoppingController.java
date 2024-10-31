package math.projeto3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import math.projeto3.RequestDTO.UpdateItemShoppingDTO;
import math.projeto3.ResponseDTO.ShoppingResponseDTO;
import math.projeto3.service.ShoppingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService){
        this.shoppingService = shoppingService;
    }

    @Operation(summary = "Novo item", description = "Adicionar novo item no carrinho do usuário", tags = "Carrinho")
    @PostMapping("/updateItem")
    public ResponseEntity<?> updateNewProductItem(@RequestBody UpdateItemShoppingDTO requestDTO){

        return shoppingService.updateQuantityToItem(requestDTO);

    }

}
