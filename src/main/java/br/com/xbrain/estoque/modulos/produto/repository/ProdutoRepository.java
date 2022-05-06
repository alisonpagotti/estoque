package br.com.xbrain.estoque.modulos.produto.repository;

import br.com.xbrain.estoque.modulos.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
