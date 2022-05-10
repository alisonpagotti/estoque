package br.com.xbrain.estoque.modulos.movimentacao.repository;

import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer> {

    List<Movimentacao> findByTipo(ETipo tipo);

    List<Movimentacao> findByDataCadastroBetween(LocalDateTime inicio, LocalDateTime fim);
}
