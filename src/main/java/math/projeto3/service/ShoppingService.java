package math.projeto3.service;

import jakarta.transaction.Transactional;
import math.projeto3.RequestDTO.NewItemShoppingRequestDTO;
import math.projeto3.RequestDTO.NewShoppingRequestDTO;
import math.projeto3.ResponseDTO.NewProductResponseDTO;
import math.projeto3.ResponseDTO.ShoppingProductDTO;
import math.projeto3.ResponseDTO.ShoppingResponseDTO;
import math.projeto3.models.ProductsModel;
import math.projeto3.models.ShoppingModel;
import math.projeto3.models.ShoppingProductModel;
import math.projeto3.models.UserModel;
import math.projeto3.repositories.ProductsRepository;
import math.projeto3.repositories.ShoppingProductRepository;
import math.projeto3.repositories.ShoppingRepository;
import math.projeto3.repositories.UsersRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Transactional
    public ResponseEntity<ShoppingResponseDTO> createShopping(NewShoppingRequestDTO newShoppingRequestDTO) {
        ShoppingModel shoppingEntity = new ShoppingModel();

        UserModel userEntity = usersRepository.findById(newShoppingRequestDTO.idUser())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        shoppingEntity.setIdUser(userEntity);

        for (NewItemShoppingRequestDTO newItem : newShoppingRequestDTO.shoppingProducts()) {
            ShoppingProductModel shoppingProductEntity = createShoppingProduct(newItem);
            shoppingEntity.addShoppingProduct(shoppingProductEntity);
        }

        double total = shoppingEntity.getShoppingProducts().stream()
                .mapToDouble(sp -> sp.getQuantity() * sp.getProduct().getPrice())
                .sum();
        shoppingEntity.setTotal(total);

        shoppingRepository.save(shoppingEntity);

        return null;
    }

    private ShoppingProductModel createShoppingProduct(NewItemShoppingRequestDTO newItem) {
        ProductsModel productEntity = productsRepository.findById(newItem.idProduct())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        ShoppingProductModel shoppingProductEntity = new ShoppingProductModel();
        shoppingProductEntity.setProduct(productEntity);
        shoppingProductEntity.setQuantity(newItem.quantity());

        return shoppingProductEntity;
    }



//    // **** Criar ou Verificar se um carrinho existe ****
//    // Identificar/Criar um carrinho
//    public Optional<ShoppingResponseDTO> identifyShopping(Long idUser){
//
//        // Verificar se existe um carrinho para esse usuário
//        Optional<ShoppingModel> shopping = verifyShopping(idUser);
//        if (shopping.isEmpty()){
//            // Se o carrinho não existir, criar um carrinho.
//            shopping = createShopping(idUser);
//            if (shopping.isEmpty()){
//                // Se o retorno for vazio, indicar que o usuário nao foi encontrado para a criação do carrinho
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado.");
//            }
//        }
//
//        // Formatar a resposta a ser enviada para o usuário
//        ShoppingResponseDTO responseDTO = new ShoppingResponseDTO();
//        responseDTO.setIdShopping(shopping.get().getIdShopping());
//        responseDTO.setIdUser(shopping.get().getIdUser().getIdUser());
//        responseDTO.setTotal(shopping.get().getTotal());
//        if (shopping.get().getShoppingProducts() == null || shopping.get().getShoppingProducts().isEmpty()){
//            responseDTO.setProducts(Collections.emptyList());
//        } else {
//            List<ShoppingProductDTO> shoppingProductDTOs = List.of();
//            for (ShoppingProductModel shoppingProductModel : shopping.get().getShoppingProducts()){
//                shoppingProductDTOs.add(new ShoppingProductDTO(
//                        shoppingProductModel.getIdShoppingProduct(),
//                        shoppingProductModel.getShopping().getIdShopping(),
//                        shoppingProductModel.getProduct().getIdProduct(),
//                        shoppingProductModel.getQuantity()
//                ));
//            }
//            responseDTO.setProducts(shoppingProductDTOs);
//        }
//
//        return Optional.of(responseDTO);
//    }
//
//    // Verificar se o carrinho já existe
//    private Optional<ShoppingModel> verifyShopping(Long idUser){
//
//        return shoppingRepository.findByIdUser_IdUser(idUser);
//    }
//
//    // Criar um carrinho novo
//    private Optional<ShoppingModel> createShopping(Long idUser){
//
//        // Verificar se o usuário existe
//        Optional<UserModel> userModel = usersRepository.findByIdUser(idUser);
//        if (userModel.isEmpty()){
//            // Se não existir, retornar vazio
//            return Optional.empty();
//        }
//
//        // Criar um novo Carrinho
//        ShoppingModel shoppingModel = new ShoppingModel();
//        shoppingModel.setIdUser(userModel.get());
//        shoppingModel.setTotal(0.0);
//        shoppingModel.setShoppingProducts(null);
//
//        // Salvar o carrinho no DB e obter o retorno com o ID
//        ShoppingModel shoppingResponseModel = shoppingRepository.save(shoppingModel);
//
//        return Optional.of(shoppingResponseModel);
//
//    }
//
//    // **** Operações dentro do carrinho ****
//    // Regsitrar novo item
//    public Optional<ShoppingResponseDTO> addNewItem(@RequestBody NewItemShoppingRequestDTO requestDTO) {
//
////        // Localizar o carrinho a partir do IdUser
////        Optional<ShoppingModel> shoppingModel = shoppingRepository.findByIdUser_IdUser(requestDTO.getIdUser());
////        // Se nao tiver carrinho, devolve um erro para o usuário
////        if (shoppingModel.isEmpty()){
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carrinho para o usuário não encontrado");
////        }
////
////        // Localizar o produto
////        Optional<ProductsModel> productsModel = productsRepository.findByIdProduct(requestDTO.getIdProduct());
////        // Se nao tiver produto, devolve um erro para o usuário
////        if (productsModel.isEmpty()){
////            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto não encontrado");
////        }
////
////        // Verificar se tem uma lista de itens no carrinho
////        if (shoppingModel.get().getShoppingProducts() == null) {
////            shoppingModel.get().setShoppingProducts(new ArrayList<>());
////        }
////
////        // Transformar em um modelo de ShoppingProduct, pois a lista do Model é com essa classe
////        ShoppingProductModel shoppingProduct = transformProduct(shoppingModel.get(), productsModel.get(), requestDTO.getQuantity());
////
////        // Adicionar o produto na lista do carrinho
////        shoppingModel.get().getShoppingProducts().add(shoppingProduct);
////
////        // Calcular valor total do carrinho
////        shoppingModel.get().setTotal(somarValores(shoppingModel.get().getShoppingProducts()));
////
////        // Salvar o carrinho
////        ShoppingModel savedShoppingModel = shoppingRepository.save(shoppingModel.get());
////
////        // Montar resposta para o usuário
////        ShoppingResponseDTO responseDTO = new ShoppingResponseDTO();
////        responseDTO.setIdShopping(savedShoppingModel.getIdShopping());
////        responseDTO.setIdUser(savedShoppingModel.getIdUser().getIdUser());
////        responseDTO.setProducts(savedShoppingModel.getShoppingProducts());
////        responseDTO.setTotal(savedShoppingModel.getTotal());
//
////        // Devolver o DTO do carrinho
////        return Optional.of(responseDTO);
//        return null;
//    }
//
//    private ShoppingProductModel transformProduct(ShoppingModel shopping ,ProductsModel product, Integer quantity){
//
//        ShoppingProductModel response = new ShoppingProductModel();
//
//        response.setShopping(shopping);
//        response.setProduct(product);
//        response.setQuantity(quantity);
//
//        response = shoppingProductRepository.save(response);
//
//        return response;
//
//    }
//
//    private Double somarValores(List<ShoppingProductModel> request){
//
//        Double soma = 0.0;
//
//        for (ShoppingProductModel produto : request){
//            Double valorProd = produto.getProduct().getPrice();
//            Integer quantity = produto.getQuantity();
//
//            soma += valorProd * quantity;
//        }
//
//        return soma;
//
//    }

}