package br.com.xbrain.estoque.modulos.movimentacao.repository;

import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {

    @Query("SELECT a FROM Movimentacao a WHERE tipo = ?1")
    List<Movimentacao> findByTipo(ETipo tipo);
}
