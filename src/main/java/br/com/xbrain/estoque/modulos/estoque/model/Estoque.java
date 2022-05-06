package br.com.xbrain.estoque.modulos.estoque.model;

import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "ESTOQUES")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME_ESTOQUE")
    private String nomeDoEstoque;
    @Column(name = "QUANTIDADE_TOTAL")
    private Integer quantidadeTotal;
    @Column(name = "VALOR_TOTAL")
    private BigDecimal valorTotal;
    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;
    @OneToMany(mappedBy = "estoque")
    private List<Produto> produto;
    @OneToMany(mappedBy = "estoque")
    private List<Movimentacao> movimentacao;

    public void atualizar(String nome) {
        this.nomeDoEstoque = nome;
    }

    public void entrada(Integer quantidade) {
        var quantidadePermitida = 100 - this.quantidadeTotal;

        if (this.quantidadeTotal + quantidade > 100) {
            throw new ArithmeticException("Quantidade ultrapassa o limite de 100 unidades do estoque!" +
                    " O estoque ainda permite a entrada de " + quantidadePermitida + " unidades.");
        } else {
            this.quantidadeTotal += quantidade;
        }
    }

    public void saida(Integer quantidade) {
        if (this.quantidadeTotal < quantidade) {
            throw new ArithmeticException("Quantidade de saída maior do que a quantidade que o estoque possui!");
        } else {
            this.quantidadeTotal -= quantidade;
        }
    }

    public void entradaValor(BigDecimal valor, Integer quantidade) {
        var total = valor.multiply(BigDecimal.valueOf(quantidade));

        this.valorTotal = this.valorTotal.add(total);
    }

    public void saidaValor(BigDecimal valor, Integer quantidade) {
        var total = valor.multiply(BigDecimal.valueOf(quantidade));

        this.valorTotal = this.valorTotal.subtract(total);
    }
}

