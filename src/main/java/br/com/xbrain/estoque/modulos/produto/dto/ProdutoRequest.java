package br.com.xbrain.estoque.modulos.produto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequest {

    @NotBlank(message = "Campo nome não pode estar em branco!")
    @Size(min = 3, max = 40, message = "O nome deve ter no mínimo 3 letras!")
    private String nomeDoProduto;
    @NotNull(message = "Campo valor do produto não pode estar em branco!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior do que zero.")
    private BigDecimal valorDoProduto;
}
