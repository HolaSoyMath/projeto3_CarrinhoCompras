package math.projeto3.service;

import math.projeto3.RequestDTO.NewItemShoppingRequestDTO;
import math.projeto3.ResponseDTO.NewProductResponseDTO;
import math.projeto3.ResponseDTO.ShoppingResponseDTO;
import math.projeto3.models.ProductsModel;
import math.projeto3.models.ShoppingModel;
import math.projeto3.models.ShoppingProductModel;
import math.projeto3.models.UserModel;
import math.projeto3.repositories.ProductsRepository;
import math.projeto3.repositories.ShoppingProductRepository;
import math.projeto3.repositories.ShoppingRepository;
import math.projeto3.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingService {

    private final ShoppingRepository shoppingRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final ShoppingProductRepository shoppingProductRepository;

    @Autowired
    public ShoppingService(ShoppingRepository shoppingRepository, UsersRepository usersRepository, ProductsRepository productsRepository, ShoppingProductRepository shoppingProductRepository) {
        this.shoppingRepository = shoppingRepository;
        this.usersRepository = usersRepository;
        this.productsRepository = productsRepository;
        this.shoppingProductRepository = shoppingProductRepository;
    }

    // **** Criar ou Verificar se um carrinho existe ****
    // Identificar/Criar um carrinho
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


    // **** Operações dentro do carrinho ****
    // Regsitrar novo item
    public Optional<ShoppingResponseDTO> addNewItem(@RequestBody NewItemShoppingRequestDTO requestDTO) {

        // Localizar o carrinho a partir do IdUser
        Optional<ShoppingModel> shoppingModel = shoppingRepository.findByIdUser_IdUser(requestDTO.getIdUser());
        // Se nao tiver carrinho, devolve um erro para o usuário
        if (shoppingModel.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carrinho para o usuário não encontrado");
        }

        // Localizar o produto
        Optional<ProductsModel> productsModel = productsRepository.findByIdProduct(requestDTO.getIdProduct());
        // Se nao tiver produto, devolve um erro para o usuário
        if (productsModel.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto não encontrado");
        }

        // Verificar se tem uma lista de itens no carrinho
        if (shoppingModel.get().getIdShoppingProduct() == null) {
            shoppingModel.get().setIdShoppingProduct(new ArrayList<>());
        }

        // Transformar em um modelo de ShoppingProduct, pois a lista do Model é com essa classe
        ShoppingProductModel shoppingProduct = transformProduct(shoppingModel.get(), productsModel.get(), requestDTO.getQuantity());

        // Adicionar o produto na lista do carrinho
        shoppingModel.get().getIdShoppingProduct().add(shoppingProduct);

        // Calcular valor total do carrinho
        shoppingModel.get().setTotal(somarValores(shoppingModel.get().getIdShoppingProduct()));

        // Salvar o carrinho
        ShoppingModel savedShoppingModel = shoppingRepository.save(shoppingModel.get());

        // Montar resposta para o usuário
        ShoppingResponseDTO responseDTO = new ShoppingResponseDTO();
        responseDTO.setIdShopping(savedShoppingModel.getIdShopping());
        responseDTO.setIdUser(savedShoppingModel.getIdUser().getIdUser());
        responseDTO.setProducts(savedShoppingModel.getIdShoppingProduct());
        responseDTO.setTotal(savedShoppingModel.getTotal());

        // Devolver o DTO do carrinho
        return Optional.of(responseDTO);
        }

    private ShoppingProductModel transformProduct(ShoppingModel shopping ,ProductsModel product, Integer quantity){

        ShoppingProductModel response = new ShoppingProductModel();

        response.setShopping(shopping);
        response.setProduct(product);
        response.setQuantity(quantity);

        response = shoppingProductRepository.save(response);

        return response;

    }

    private Double somarValores(List<ShoppingProductModel> request){

        Double soma = 0.0;

        for (ShoppingProductModel produto : request){
            Double valorProd = produto.getProduct().getPrice();
            Integer quantity = produto.getQuantity();

            soma += valorProd * quantity;
        }

        return soma;

    }

}