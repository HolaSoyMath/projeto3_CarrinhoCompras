package math.projeto3.repositories;

import math.projeto3.models.ShoppingProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingProductRepository extends JpaRepository<ShoppingProductModel, Long> {
}
