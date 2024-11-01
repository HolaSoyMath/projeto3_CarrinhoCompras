package math.projeto3.service;

import jakarta.transaction.Transactional;
import math.projeto3.RequestDTO.UpdateItemShoppingDTO;
import math.projeto3.RequestDTO.NewItemShoppingRequestDTO;
import math.projeto3.RequestDTO.NewShoppingRequestDTO;
import math.projeto3.ResponseDTO.FinishShoppingDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        // Criar um novo Carrinho
        ShoppingModel shoppingEntity = new ShoppingModel();

        // Obter o Usuário, caso nao encontre, retornar o aviso de erro, se der certo, setar o objeto Usuário no "idUser"
        UserModel userEntity = usersRepository.findById(newShoppingRequestDTO.idUser())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        shoppingEntity.setIdUser(userEntity);

        // Varrer a lista de produtos e adicionar cada produto no campo de "produtos"
        for (NewItemShoppingRequestDTO newItem : newShoppingRequestDTO.shoppingProducts()) {
            // Com o Shopping criado, enviar para o método de adicionar novo produto
            shoppingEntity.addShoppingProduct(createShoppingProduct(newItem));
        }

        // Varrer a lista de todos os produtos e somar os valores do "price"
        double total = getTotal(shoppingEntity);
        // Setar o valor total no campo específico
        shoppingEntity.setTotal(total);

        // Salvar a entidade
        shoppingRepository.save(shoppingEntity);

        return null;
    }

    // Função para verificar se o usuário possui um carrinho registrado
    private ShoppingModel verifyShopping(Long idUser){

        // Obter carrinho do usuário
        Optional<ShoppingModel> shoppingModel = Optional.ofNullable(shoppingRepository.findByUser_idUser(idUser));

        // Verificar se o carrinho existe
        if (shoppingModel.isEmpty()){
            ShoppingModel newShoppingModel =  new ShoppingModel();
            newShoppingModel.setIdUser(usersRepository.findByIdUser(idUser).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado")));
            newShoppingModel.setShoppingProducts(new ArrayList<>());
            shoppingModel = Optional.of(shoppingRepository.save(newShoppingModel));
        }

        return shoppingModel.get();

    }

    // Função para gerar a soma total dos produtos em um carrinho
    private static double getTotal(ShoppingModel shoppingEntity) {
        return shoppingEntity.getShoppingProducts().stream()
                .mapToDouble(sp -> sp.getQuantity() * sp.getProduct().getPrice())
                .sum();
    }

    private ShoppingProductModel createShoppingProduct(NewItemShoppingRequestDTO newItem) {
        // Buscar o produto por ID
        ProductsModel productEntity = productsRepository.findById(newItem.idProduct())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        // Criar um registro de shoppingProduct
        ShoppingProductModel shoppingProductEntity = new ShoppingProductModel();
        // No shopping criado, setar a entidade Product encontrada anteriormente
        shoppingProductEntity.setProduct(productEntity);
        // No shopping criado, setar a quantidade daquele item
        shoppingProductEntity.setQuantity(newItem.quantity());

        return shoppingProductEntity;
    }

    public ResponseEntity<?> updateQuantityToItem(UpdateItemShoppingDTO updateItemShoppingDTO){
        // Verificar se o carrinho do usuário existe, caso nao exista, criar
        ShoppingModel shoppingModel = verifyShopping(updateItemShoppingDTO.idUser());

        // Verificar se o item existe
        ProductsModel productsModel = productsRepository.findById(updateItemShoppingDTO.idProduct())
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID informado"));

        // Mapear todos os ID's de produtos
        Map<Long, ShoppingProductModel> productMap = shoppingModel.getShoppingProducts()
                .stream()
                .collect(Collectors.toMap(p -> p.getProduct().getIdProduct(), p -> p));

        // Alterar a quantidade
        boolean verif = updateQuantityAdd(updateItemShoppingDTO, productMap, shoppingModel, productsModel);
        if (!verif){
            return new ResponseEntity<>("Quantidade inferior a 0, não foi possível adicionar o produto", HttpStatus.BAD_REQUEST);
        }

        // Calcular o total
        shoppingModel.setTotal(getTotal(shoppingModel));

        // Salvar registro
        shoppingModel = shoppingRepository.save(shoppingModel);
        
        // Colocar no formato do DTO
        ShoppingResponseDTO responseDTO = new ShoppingResponseDTO(
                shoppingModel.getIdShopping(),
                shoppingModel.getIdUser().getIdUser(),
                shoppingModel.getTotal(),
                toDto(shoppingModel.getShoppingProducts())
                );

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    private boolean updateQuantityAdd(UpdateItemShoppingDTO updateItemShoppingDTO, Map<Long, ShoppingProductModel> productMap, ShoppingModel shoppingModel, ProductsModel productsModel) {
        if (productMap.containsKey(updateItemShoppingDTO.idProduct())){
            // Se possuir, adicionar o item
            ShoppingProductModel modifyProduct = productMap.get(updateItemShoppingDTO.idProduct());
            modifyProduct.setQuantity(modifyProduct.getQuantity() + updateItemShoppingDTO.quantity());
            // Verificar se o valor é zero ou menor para retirar da lista e do mapping
            if (modifyProduct.getQuantity() <= 0){
                shoppingModel.getShoppingProducts().remove(modifyProduct);
                shoppingProductRepository.delete(modifyProduct);
                productMap.remove(updateItemShoppingDTO.idProduct());
            }

        } else{
            if (updateItemShoppingDTO.quantity() > 0) {
                // Se não possuir, criar novo item no carrinho
                ShoppingProductModel newProduct = new ShoppingProductModel();
                newProduct.setShopping(shoppingModel);
                newProduct.setProduct(productsModel);
                newProduct.setQuantity(updateItemShoppingDTO.quantity());

                newProduct = shoppingProductRepository.save(newProduct);

                shoppingModel.getShoppingProducts().add(newProduct);
            } else {
                // Indicar que deu errada atualização
                return false;
            }
        }
        return true;
    }

    private static List<ShoppingProductDTO> toDto(List<ShoppingProductModel> shoppingProductModels) {

        List<ShoppingProductDTO> shoppingProductDTOS = new ArrayList<>();
        for (ShoppingProductModel item : shoppingProductModels) {
            shoppingProductDTOS.add(new ShoppingProductDTO(
                    item.getIdShoppingProduct(),
                    item.getShopping().getIdShopping(),
                    item.getProduct().getIdProduct(),
                    item.getQuantity()
            ));
        }

        return shoppingProductDTOS;
    }

    public FinishShoppingDTO finishShopping (Long idUser){

        ShoppingModel a = shoppingRepository.findByUser_idUser(idUser);

        FinishShoppingDTO response = new FinishShoppingDTO(a.getIdUser().getIdUser(),
                a.getTotal());

        shoppingRepository.deleteById(a.getIdShopping());

        // Retornar a entidade
        return response;

    }
}

