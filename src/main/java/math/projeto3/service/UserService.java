package math.projeto3.service;

import math.projeto3.RequestDTO.RegisterUserRequestDTO;
import math.projeto3.ResponseDTO.RegisterUserResponseDTO;
import math.projeto3.models.UserModel;
import math.projeto3.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private final UsersRepository repository;

    public UserService(UsersRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<RegisterUserResponseDTO> createUser(RegisterUserRequestDTO userDTO) {

        // Verificar se já existe o usuário cadastrado
        if (repository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário já está em uso.");
        }

        // Transformar o DTO em Model para salvar no DB
        UserModel usuario = new UserModel();
        usuario.setUsername(userDTO.getUsername());
        usuario.setPassword(userDTO.getPassword());

        try {
            // Tenta salvar o usuário, caso consiga, retorna as informações do registro
            UserModel responseModel = repository.save(usuario);

            // Transformar o Model em ResponseDTO
            RegisterUserResponseDTO responseDTO = new RegisterUserResponseDTO();
            responseDTO.setIdUser(responseModel.getIdUser());
            responseDTO.setUsername(responseModel.getUsername());

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            // Caso não consiga, indica que teve um erro ao salvar o usuário
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o usuário.");
        }
    }
}
