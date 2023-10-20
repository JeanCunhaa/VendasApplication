package domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import domain.entity.ItemPedido;

public interface ItensPedido extends JpaRepository< ItemPedido, Integer > {
}
