package br.com.xbrain.estoque.modulos.backup.produto.repository;

import br.com.xbrain.estoque.modulos.backup.produto.model.BackupProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupProdutoRepository extends JpaRepository<BackupProduto, Integer> {
}
