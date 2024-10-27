package math.projeto3.repositories;

import math.projeto3.models.ShoppingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingRepository extends JpaRepository<ShoppingModel, Long> {
    Optional<ShoppingModel> findByIdUser_IdUser(Long idUser);
}
