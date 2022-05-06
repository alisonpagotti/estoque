package br.com.xbrain.estoque.modulos.movimentacao;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.produto.model.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MovimentacaoHelper {

    public static Produto umProduto(Integer id, String nome) {
        return Produto.builder()
                .id(id)
                .nomeDoProduto(nome)
                .quantidade(0)
                .valorDoProduto(new BigDecimal(3.0))
                .build();
    }

    public static Estoque umEstoque(Integer id, String nome) {
        return Estoque.builder()
                .id(id)
                .nomeDoEstoque(nome)
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0))
                .build();
    }
}
