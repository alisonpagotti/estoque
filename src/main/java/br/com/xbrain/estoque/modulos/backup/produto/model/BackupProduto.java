package br.com.xbrain.estoque.modulos.backup.produto.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "BACKUP_PRODUTOS")
public class BackupProduto implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "VALOR_PRODUTO")
    private BigDecimal valorDoProduto;
    @Column(name = "QUANTIDADE")
    private Integer quantidade;
}
