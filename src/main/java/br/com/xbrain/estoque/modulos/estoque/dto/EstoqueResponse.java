package br.com.xbrain.estoque.modulos.estoque.dto;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstoqueResponse {

    private Integer id;
    private String nomeDoEstoque;
    private Integer quantidadeTotal;
    private List<String> produtos;
    private BigDecimal valorTotal;
    @JsonFormat(pattern="dd-MM-yyyy 'às' HH:mm:ss")
    private LocalDateTime dataCadastro;

    public static EstoqueResponse of(Estoque estoque) {
        var produtos = estoque.getProduto();
        var listaProdutos = produtos.stream().map(Produto::getNomeDoProduto).collect(Collectors.toList());

      return EstoqueResponse.builder()
              .id(estoque.getId())
              .nomeDoEstoque(estoque.getNomeDoEstoque())
              .quantidadeTotal(estoque.getQuantidadeTotal())
              .produtos(listaProdutos)
              .valorTotal(estoque.getValorTotal())
              .dataCadastro(estoque.getDataCadastro())
              .build();
    }

    public static List<EstoqueResponse> of(List<Estoque> estoque){
        return estoque.stream().map(EstoqueResponse::of).collect(Collectors.toList());
    }
}
