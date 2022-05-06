package br.com.xbrain.estoque.modulos.produto.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponse {

    private Integer id;
    private String nomeDoProduto;
    private BigDecimal valorDoProduto;
    private Integer quantidade;
    @JsonFormat(pattern="dd-MM-yyyy 'às' HH:mm:ss")
    private LocalDateTime dataCadastro;

    public static ProdutoResponse of(Produto produto) {
        return ProdutoResponse.builder()
                .id(produto.getId())
                .nomeDoProduto(produto.getNomeDoProduto())
                .valorDoProduto(produto.getValorDoProduto())
                .quantidade(produto.getQuantidade())
                .dataCadastro(produto.getDataCadastro())
                .build();
    }

    public static List<ProdutoResponse> of(List<Produto> produto) {
        return produto.stream().map(ProdutoResponse::of).collect(Collectors.toList());
    }
}
