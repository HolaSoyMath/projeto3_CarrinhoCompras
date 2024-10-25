package math.projeto3.controllers;

import math.projeto3.models.UserModel;
import math.projeto3.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UsersRepository repository;

    public UserController(UsersRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UserModel> salvar(@RequestBody UserModel usuario) {

        // Verificar se já existe o usuário cadastrado
        if (repository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário já está em uso.");
        }

        try {
            // Tenta salvar o usuário, caso consiga, retorna as informações do registro
            usuario = repository.save(usuario);
            return ResponseEntity.ok().body(usuario);
        } catch (Exception e) {
            // Caso não consiga, indica que teve um erro ao salvar o usuário
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o usuário.");
        }

    }
}
