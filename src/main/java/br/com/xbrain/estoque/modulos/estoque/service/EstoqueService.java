package br.com.xbrain.estoque.modulos.estoque.service;

import br.com.xbrain.estoque.modulos.estoque.dto.AtualizarEstoqueRequest;
import br.com.xbrain.estoque.modulos.estoque.dto.EstoqueRequest;
import br.com.xbrain.estoque.modulos.estoque.dto.EstoqueResponse;
import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.estoque.repository.EstoqueRepository;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.rabbitmq.EnviarFilaProduto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository repository;

    @Autowired
    private EnviarFilaProduto enviarFilaProduto;

    private final String EX_ESTOQUE_NAO_CADASTRADO = "Estoque não cadastrado!";

    public List<EstoqueResponse> listarTodos() {
        var estoque = repository.findAll();

        return EstoqueResponse.of(estoque);
    }

    public EstoqueResponse detalhar(Integer id) {
        try {
            var estoque = repository.getById(id);

            return EstoqueResponse.of(estoque);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_ESTOQUE_NAO_CADASTRADO);
        }
    }

    @Transactional
    public EstoqueResponse cadastrar(EstoqueRequest request) {

        var estoque = Estoque.builder()
                .nomeDoEstoque(request.getNomeDoEstoque())
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .dataCadastro(LocalDateTime.now())
                .build();

        repository.save(estoque);

        return EstoqueResponse.of(estoque);
    }

    @Transactional
    public EstoqueResponse atualizar(Integer id, AtualizarEstoqueRequest request) {
        try {
            var estoque = repository.getById(id);
            estoque.setNomeDoEstoque(request.getNome());

            return EstoqueResponse.of(estoque);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_ESTOQUE_NAO_CADASTRADO);
        }
    }

    @Transactional
    public void remover(Integer id) {
        try {
            repository.deleteById(id);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_ESTOQUE_NAO_CADASTRADO);
        }
    }
}
