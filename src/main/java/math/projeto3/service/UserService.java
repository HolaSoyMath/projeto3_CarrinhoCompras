package math.projeto3.service;

import math.projeto3.RequestDTO.NewUserRequestDTO;
import math.projeto3.ResponseDTO.NewUserResponseDTO;
import math.projeto3.models.UserModel;
import math.projeto3.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UsersRepository repository;

    public UserService(UsersRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<NewUserResponseDTO> createUser(NewUserRequestDTO newUserRequestDTO) {

        // Verificar se já existe o usuário cadastrado
        if (repository.findByUsername(newUserRequestDTO.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome de usuário já está em uso.");
        }

        // Transformar o DTO em Model para salvar no DB
        UserModel userEntity = new UserModel();
        userEntity.setUsername(newUserRequestDTO.username());
        userEntity.setPassword(newUserRequestDTO.password());

        try {
            // Tenta salvar o usuário, caso consiga, retorna as informações do registro
            UserModel responseModel = repository.save(userEntity);

            // Transformar o Model em ResponseDTO
            NewUserResponseDTO newUserResponseDTO = new NewUserResponseDTO();
            newUserResponseDTO.setIdUser(responseModel.getIdUser());
            newUserResponseDTO.setUsername(responseModel.getUsername());

            return ResponseEntity.ok().body(newUserResponseDTO);
        } catch (Exception e) {
            // Caso não consiga, indica que teve um erro ao salvar o usuário
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar o usuário.");
        }
    }

    public ResponseEntity<List<NewUserResponseDTO>> getAll() {
        List<UserModel> userEntities = repository.findAll();
        List<NewUserResponseDTO> newUserResponseDTOS = new ArrayList<>();
        for (UserModel userEntity : userEntities) {
            NewUserResponseDTO newUserResponseDTO = new NewUserResponseDTO();
            newUserResponseDTO.setIdUser(userEntity.getIdUser());
            newUserResponseDTO.setUsername(userEntity.getUsername());
            newUserResponseDTOS.add(newUserResponseDTO);
        }
        return ResponseEntity.ok().body(newUserResponseDTOS);
    }
}
