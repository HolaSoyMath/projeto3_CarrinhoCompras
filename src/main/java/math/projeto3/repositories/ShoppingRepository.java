package math.projeto3.repositories;

import math.projeto3.models.ShoppingModel;
import math.projeto3.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingRepository extends JpaRepository<ShoppingModel, Long> {
    Optional<ShoppingModel> findByUser(UserModel user);
    ShoppingModel findByUser_idUser(Long idUser);
}
