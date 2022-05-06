package br.com.xbrain.estoque.modulos.movimentacao.dto;

import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class MovimentacaoRequest {

    @NotNull
    private ETipo tipo;
    @NotNull
    private Integer idProduto;
    @NotNull
    private Integer idEstoque;
    @NotNull
    @Min(value = 1, message = "O valor mínimo deve ser maior do que zero!")
    private Integer quantidade;
    @Length(max = 100, message = "O máximo permitido são 100 caracteres!")
    private String observacao;
}
