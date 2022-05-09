package br.com.xbrain.estoque.modulos.produto.service;

import br.com.xbrain.estoque.modulos.backup.produto.service.BackupProdutoService;
import br.com.xbrain.estoque.modulos.estoque.repository.EstoqueRepository;
import br.com.xbrain.estoque.modulos.produto.dto.AtualizarProdutoRequest;
import br.com.xbrain.estoque.modulos.produto.dto.ProdutoRequest;
import br.com.xbrain.estoque.modulos.produto.dto.ProdutoResponse;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import br.com.xbrain.estoque.modulos.produto.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private BackupProdutoService backupProdutoService;

    @Autowired
    private EstoqueRepository estoqueRepository;

    private final String EX_PRODUTO_NAO_CADASTRADO = "Produto não cadastrado!";
    private final String EX_PRODUTO_JA_CADASTRADO = "Produto já cadastrado!";

    public List<ProdutoResponse> listarTodos() {
        var produto = repository.findAll();

        return ProdutoResponse.of(produto);
    }

    public ProdutoResponse detalhar(Integer id) {
        try {
            var produto = repository.getById(id);

            return ProdutoResponse.of(produto);

        } catch (Exception e) {
            throw new EntityNotFoundException(EX_PRODUTO_NAO_CADASTRADO);
        }
    }

    @Transactional
    public ProdutoResponse cadastrar(ProdutoRequest request) {
        try {
            var produto = Produto.builder()
                    .nomeDoProduto(request.getNomeDoProduto())
                    .valorDoProduto(request.getValorDoProduto())
                    .quantidade(0)
                    .dataCadastro(LocalDateTime.now())
                    .build();

            repository.save(produto);
            backupProdutoService.gerarBackupProduto(produto);

            return ProdutoResponse.of(produto);

        } catch (Exception ex) {
            throw new DataIntegrityViolationException(EX_PRODUTO_JA_CADASTRADO);
        }
    }

    @Transactional
    public ProdutoResponse atualizar(Integer id, AtualizarProdutoRequest request) {
        try {
            var produto = repository.getById(id);
            produto.atualizar(request.getNome(), request.getValorDoProduto());

            return ProdutoResponse.of(produto);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_PRODUTO_NAO_CADASTRADO);
        }
    }

    @Transactional
    public void remover(Integer id) {
        try {
            repository.deleteById(id);

        } catch (Exception ex) {
            throw new EntityNotFoundException(EX_PRODUTO_NAO_CADASTRADO);
        }
    }
}
