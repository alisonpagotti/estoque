package br.com.xbrain.estoque.modulos.movimentacao.dto;

import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MovimentacaoResponse {

    private Integer id;
    private ETipo tipo;
    private Integer quantidade;
    private String nomeProduto;
    private String nomeEstoque;
    private String observacao;
    @JsonFormat(pattern="dd-MM-yyyy 'às' HH:mm:ss")
    private LocalDateTime dataCadastro;

    public static MovimentacaoResponse of(Movimentacao movimentacao) {
        var produto = movimentacao.getProduto();
        var estoque = movimentacao.getEstoque();

        return MovimentacaoResponse.builder()
                .id(movimentacao.getId())
                .tipo(movimentacao.getTipo())
                .quantidade(movimentacao.getQuantidade())
                .nomeProduto(produto.getNomeDoProduto())
                .nomeEstoque(estoque.getNomeDoEstoque())
                .observacao(movimentacao.getObservacao())
                .dataCadastro(movimentacao.getDataCadastro())
                .build();
    }

    public static List<MovimentacaoResponse> of(List<Movimentacao> movimentacao) {
        return movimentacao.stream().map(MovimentacaoResponse::of).collect(Collectors.toList());
    }
}
