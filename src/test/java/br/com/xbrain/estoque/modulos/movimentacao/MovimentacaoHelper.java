package br.com.xbrain.estoque.modulos.movimentacao;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import br.com.xbrain.estoque.modulos.produto.model.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<Movimentacao> umaListaMovimentacaoEntrada() {
        return Arrays.asList(
                Movimentacao.builder()
                        .id(1)
                        .tipo(ETipo.ENTRADA)
                        .produto(umProduto(1, "Caneta Preta"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Entrada de 10 Canetas Pretas")
                        .build(),

                Movimentacao.builder()
                        .id(2)
                        .tipo(ETipo.ENTRADA)
                        .produto(umProduto(2, "Caneta Azul"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Entrada de 10 Canetas Azuis")
                        .build()
        );
    }

    public static List<Movimentacao> umaListaMovimentacaoSaida() {
        return Arrays.asList(
                Movimentacao.builder()
                        .id(1)
                        .tipo(ETipo.SAIDA)
                        .produto(umProduto(1, "Caneta Preta"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Saída de 10 Canetas Pretas")
                        .build(),

                Movimentacao.builder()
                        .id(2)
                        .tipo(ETipo.SAIDA)
                        .produto(umProduto(2, "Caneta Azul"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Saída de 10 Canetas Azuis")
                        .build()
        );
    }
}
