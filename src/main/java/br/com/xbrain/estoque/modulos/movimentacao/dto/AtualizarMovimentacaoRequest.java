package br.com.xbrain.estoque.modulos.movimentacao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtualizarMovimentacaoRequest {

    @Length(max = 100, message = "O máximo permitido são 100 caracteres!")
    private String observacao;
}
