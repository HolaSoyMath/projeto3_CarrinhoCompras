package math.projeto3.repositories;

import math.projeto3.models.ProductsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsModel, Long> {
    Optional<ProductsModel> findByName(String name);
}
