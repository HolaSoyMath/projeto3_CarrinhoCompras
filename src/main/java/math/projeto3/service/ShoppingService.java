package math.projeto3.service;

import math.projeto3.ResponseDTO.ShoppingResponseDTO;
import math.projeto3.models.ProductsModel;
import math.projeto3.models.ShoppingModel;
import math.projeto3.models.UserModel;
import math.projeto3.repositories.ProductsRepository;
import math.projeto3.repositories.ShoppingRepository;
import math.projeto3.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingService {

    private final ShoppingRepository shoppingRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;

    @Autowired
    public ShoppingService(ShoppingRepository shoppingRepository, UsersRepository usersRepository, ProductsRepository productsRepository) {
        this.shoppingRepository = shoppingRepository;
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
    }

    // Registrar novo item no carrinho
    public Optional<ShoppingResponseDTO> identifyShopping(Long idUser){

        // Verificar se existe um carrinho para esse usuário
        Optional<ShoppingModel> shopping = verifyShopping(idUser);
        if (shopping.isEmpty()){
            // Se o carrinho não existir, criar um carrinho.
            shopping = createShopping(idUser);
            if (shopping.isEmpty()){
                // Se o retorno for vazio, indicar que o usuário nao foi encontrado para a criação do carrinho
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado.");
            }
        }

        // Formatar a resposta a ser enviada para o usuário
        ShoppingResponseDTO responseDTO = new ShoppingResponseDTO();
        responseDTO.setIdShopping(shopping.get().getIdShopping());
        responseDTO.setIdUser(shopping.get().getIdUser().getIdUser());
        responseDTO.setTotal(shopping.get().getTotal());
        if (shopping.get().getIdShoppingProduct() == null || shopping.get().getIdShoppingProduct().isEmpty()){
            responseDTO.setProducts(Collections.emptyList());
        } else {
            responseDTO.setProducts(shopping.get().getIdShoppingProduct());
        }

        return Optional.of(responseDTO);
    }

    // Verificar se o carrinho já existe
    private Optional<ShoppingModel> verifyShopping(Long idUser){

        return shoppingRepository.findByIdUser_IdUser(idUser);
    }

    // Criar um carrinho novo
    private Optional<ShoppingModel> createShopping(Long idUser){

        // Verificar se o usuário existe
        Optional<UserModel> userModel = usersRepository.findByIdUser(idUser);
        if (userModel.isEmpty()){
            // Se não existir, retornar vazio
            return Optional.empty();
        }

        // Criar um novo Carrinho
        ShoppingModel shoppingModel = new ShoppingModel();
        shoppingModel.setIdUser(userModel.get());
        shoppingModel.setTotal(0.0);
        shoppingModel.setIdShoppingProduct(null);

        // Salvar o carrinho no DB e obter o retorno com o ID
        ShoppingModel shoppingResponseModel = shoppingRepository.save(shoppingModel);

        return Optional.of(shoppingResponseModel);

    }

}