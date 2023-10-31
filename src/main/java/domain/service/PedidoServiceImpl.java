package domain.service;


import domain.dto.ItemPedidoDTO;
import domain.dto.PedidoDTO;
import domain.entity.Cliente;
import domain.entity.ItemPedido;
import domain.entity.Pedido;
import domain.entity.Produto;
import domain.exception.RegraNegocioException;
import domain.repository.Clientes;
import domain.repository.ItensPedido;
import domain.repository.Pedidos;
import domain.repository.Produtos;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService{

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedido itensPedidoRepository;



    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository.
                findById(idCliente).
                orElseThrow(() -> new RegraNegocioException("Código de cliente invalido"));
        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);


        List<ItemPedido> itensPedidos = converterItens(pedido, dto.getItens());
        pedido.setTotal(calcularTotalPedido(itensPedidos));
        repository.save(pedido);
        itensPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return pedido;
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possivel realizar um pedido sem itens.");
        }

        return itens.stream().map( dto -> {
            Integer idProduto = dto.getProduto();
            Produto produto = produtosRepository.findById(idProduto).orElseThrow(() -> new RegraNegocioException(
                    "Código de produto inválido: " + idProduto
            ));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());

    }

    private BigDecimal calcularTotalPedido(List<ItemPedido> itens) {
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedido item : itens) {
            BigDecimal subtotalItem = item.getProduto().getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(subtotalItem);
        }

        return total;
    }


    // TODO: 31/10/2023 : eu quero que voce faca um endpoint, que dado duas datas, data inicial e final, sendo que a final, não pode
    // ser inferior a incial, quero que voce liste todos os clientes que fizeram pedidos nesse intervalo de tempo,
    // bem como seus pedidos, e eu nao posso recuperar todos os pedidos e clientes de uma vez, recurar filtrados e
    // organizar no service.

    // TODO: recuperar cliente que fez mais pedidos no ano de 2023

}
