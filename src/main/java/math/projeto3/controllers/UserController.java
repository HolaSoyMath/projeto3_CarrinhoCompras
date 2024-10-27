package math.projeto3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import math.projeto3.RequestDTO.NewUserRequestDTO;
import math.projeto3.ResponseDTO.NewUserResponseDTO;
import math.projeto3.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Cadastrar um novo usuário no sistema",
            tags = "Usuário")
    @PostMapping("/register")
    public ResponseEntity<NewUserResponseDTO> salvar(@RequestBody NewUserRequestDTO newUserRequestDTO) {
        return userService.createUser(newUserRequestDTO);
    }

    @GetMapping
    public ResponseEntity<List<NewUserResponseDTO>> getUser() {
        return userService.getAll();
    }
}
