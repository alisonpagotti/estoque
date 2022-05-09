package br.com.xbrain.estoque.modulos.movimentacao;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import org.apache.tomcat.jni.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MovimentacaoHelper {

    public static Produto umProduto(Integer id, String nome) {
        return Produto.builder()
                .id(id)
                .nomeDoProduto(nome)
                .quantidade(0)
                .valorDoProduto(new BigDecimal(3.0))
                .dataCadastro(LocalDateTime.now())
                .build();
    }

    public static Produto umProdutoDataCadastro(Integer id, String nome, LocalDateTime dataCadastro) {
        return Produto.builder()
                .id(id)
                .nomeDoProduto(nome)
                .quantidade(0)
                .valorDoProduto(new BigDecimal(3.0))
                .dataCadastro(dataCadastro)
                .build();
    }

    public static Estoque umEstoque(Integer id, String nome) {
        return Estoque.builder()
                .id(id)
                .nomeDoEstoque(nome)
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0))
                .dataCadastro(LocalDateTime.now())
                .build();
    }

    public static Estoque umEstoqueDataCadastro(Integer id, String nome, LocalDateTime dataCadastro) {
        return Estoque.builder()
                .id(id)
                .nomeDoEstoque(nome)
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0))
                .dataCadastro(dataCadastro)
                .build();
    }

    public static Produto umProdutoQuantidade(Integer id, String nome, Integer quantidade) {
        return Produto.builder()
                .id(id)
                .nomeDoProduto(nome)
                .quantidade(quantidade)
                .valorDoProduto(new BigDecimal(3.0))
                .build();
    }

    public static Estoque umEstoqueQuantidade(Integer id, String nome, Integer quantidade) {
        return Estoque.builder()
                .id(id)
                .nomeDoEstoque(nome)
                .quantidadeTotal(quantidade)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0))
                .build();
    }
}
