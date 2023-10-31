package domain.service;

import domain.controller.PedidoController;
import domain.dto.PedidoDTO;
import domain.entity.Pedido;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public interface PedidoService {
    Pedido salvar( PedidoDTO dto );

}
