package br.com.xbrain.estoque.modulos.estoque.repository;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
}
