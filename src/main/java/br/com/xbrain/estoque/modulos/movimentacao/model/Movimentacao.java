package br.com.xbrain.estoque.modulos.movimentacao.model;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "MOVIMENTACOES")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TIPO")
    @Enumerated(EnumType.STRING)
    private ETipo tipo;
    @Column(name = "QUANTIDADE")
    private Integer quantidade;
    @Column(name = "OBSERVACAO")
    private String observacao;
    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "produto")
    private Produto produto;
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "estoque")
    private Estoque estoque;
}