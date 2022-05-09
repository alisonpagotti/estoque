package br.com.xbrain.estoque.modulos.produto.model;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "PRODUTOS")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME_PRODUTO", unique = true)
    private String nomeDoProduto;
    @Column(name = "VALOR_PRODUTO")
    private BigDecimal valorDoProduto;
    @Column(name = "QUANTIDADE")
    private Integer quantidade;
    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;
    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    private List<Movimentacao> movimentacoes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id")
    private Estoque estoque;

    public void of(Estoque estoque) {
        this.estoque = estoque;
    }

    public void validarEstoque(Integer quantidade) {
        if (this.quantidade == 0) {
            this.estoque = null;
        } else {
            this.estoque = estoque;
        }
    }

    public void entrada(Integer quantidade) {
        this.quantidade += quantidade;
    }

    public void saida(Integer quantidade) {
        if (this.quantidade < quantidade) {
            throw new ArithmeticException("Quantidade de saída maior do que a quantidade que o estoque possui!");
        }
        this.quantidade -= quantidade;
    }
}
