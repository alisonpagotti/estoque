package br.com.xbrain.estoque.modulos.estoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstoqueRequest {

    @NotBlank(message = "Campo nome não pode estar em branco!")
    @Size(min = 3, max = 40, message = "O nome deve ter no mínimo 3 letras!")
    private String nomeDoEstoque;
}
