package br.com.xbrain.estoque.modulos.movimentacao.service;

import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.estoque.repository.EstoqueRepository;
import br.com.xbrain.estoque.modulos.movimentacao.dto.AtualizarMovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoResponse;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import br.com.xbrain.estoque.modulos.movimentacao.repository.MovimentacaoRepository;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import br.com.xbrain.estoque.modulos.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo.ENTRADA;
import static br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo.SAIDA;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    private final String EX_TIPO_MOVIMENTACAO_NAO_CADASTRADA = "Tipo de movimentação não cadastrada!";
    private final String EX_MOVIMENTACAO_NAO_CADASTRADA = "Movimentação não cadastrada!";

    public List<MovimentacaoResponse> listarTodas() {
        var movimentacoes = repository.findAll();

        return MovimentacaoResponse.of(movimentacoes);
    }

    public List<MovimentacaoResponse> listarPorTipoMovimentacao(String tipo) {
        try {
            var eTipo = ETipo.valueOf(tipo.toUpperCase());
            var listaPorTipo = repository.findByTipo(eTipo);

            return MovimentacaoResponse.of(listaPorTipo);

        } catch (Exception ex) {
            throw new IllegalArgumentException(EX_TIPO_MOVIMENTACAO_NAO_CADASTRADA);
        }
    }

    public MovimentacaoResponse detalhar(Integer id) {
        try {
            var movimentacao = repository.getById(id);

            return MovimentacaoResponse.of(movimentacao);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_MOVIMENTACAO_NAO_CADASTRADA);
        }
    }

    @Transactional
    public MovimentacaoResponse movimentacao(MovimentacaoRequest request) {
        var produto = produtoRepository.getById(request.getIdProduto());
        var estoque = estoqueRepository.getById(request.getIdEstoque());

        if (request.getTipo() == ENTRADA) {
            return salvarEntrada(produto, estoque, request);

        } else {
            return salvarSaida(produto, estoque, request);
        }
    }

    private MovimentacaoResponse salvarEntrada(Produto produto, Estoque estoque, MovimentacaoRequest request) {
        produto.of(estoque);
        estoque.entrada(request.getQuantidade());
        produto.entrada(request.getQuantidade());
        estoque.entradaValor(produto.getValorDoProduto(), request.getQuantidade());

        var movimentacao = Movimentacao.builder()
                .tipo(request.getTipo())
                .produto(produto)
                .quantidade(request.getQuantidade())
                .estoque(estoque)
                .observacao(request.getObservacao())
                .dataCadastro(LocalDateTime.now())
                .build();

        repository.save(movimentacao);

        return MovimentacaoResponse.of(movimentacao);
    }

    private MovimentacaoResponse salvarSaida(Produto produto, Estoque estoque, MovimentacaoRequest request) {
        estoque.saida(request.getQuantidade());
        produto.saida(request.getQuantidade());
        produto.validarEstoque(request.getQuantidade());
        estoque.saidaValor(produto.getValorDoProduto(), request.getQuantidade());

        var movimentacao = Movimentacao.builder()
                .tipo(request.getTipo())
                .produto(produto)
                .quantidade(request.getQuantidade())
                .estoque((estoque))
                .observacao(request.getObservacao())
                .dataCadastro(LocalDateTime.now())
                .build();

        repository.save(movimentacao);

        return MovimentacaoResponse.of(movimentacao);
    }

    @Transactional
    public MovimentacaoResponse atualizar(Integer id, AtualizarMovimentacaoRequest request) {
        try {
            var movimentacao = repository.getById(id);
            movimentacao.atualizar(request.getObservacao());

            return MovimentacaoResponse.of(movimentacao);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_MOVIMENTACAO_NAO_CADASTRADA);
        }
    }
}