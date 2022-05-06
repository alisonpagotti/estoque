package br.com.xbrain.estoque.modulos.produto.service;

import br.com.xbrain.estoque.modulos.produto.dto.AtualizarProdutoRequest;
import br.com.xbrain.estoque.modulos.produto.dto.ProdutoRequest;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import br.com.xbrain.estoque.modulos.produto.repository.ProdutoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @Test
    public void detalhar_porProduto_sucesso() throws Exception {

        var produto = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Preta")
                .valorDoProduto(new BigDecimal(3.0))
                .build();

        when(repository.getById(1)).thenReturn(produto);

        var produtoDetalhado = service.detalhar(produto.getId());

        assertEquals(produto.getId(), produtoDetalhado.getId());

        verify(repository, times(1)).getById(produto.getId());
    }

    @Test
    public void detalhar_porProduto_NaoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Produto não cadastrado!")).when(repository).getById(1);

        assertThatThrownBy(() -> service.detalhar(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Produto não cadastrado!");

        verify(repository, times(1)).getById(1);
    }

    @Test
    public void cadastrar_produto_sucesso() throws Exception {

        var produto = Produto.builder()
                .nomeDoProduto("Caneta Preta")
                .valorDoProduto(new BigDecimal(3.0))
                .quantidade(0)
                .build();

        var produtoRequest = ProdutoRequest.builder()
                .nomeDoProduto("Caneta Preta")
                .valorDoProduto(new BigDecimal(3.0))
                .build();

        var produtoCadastrado = service.cadastrar(produtoRequest);

        assertEquals(produto.getNomeDoProduto(), produtoCadastrado.getNomeDoProduto());

        verify(repository, times(1)).save(produto);
    }

    @Test
    public void atualizar_produto_sucesso() throws Exception {

        var produto = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Preta Atualizada")
                .valorDoProduto(new BigDecimal(3.5))
                .quantidade(0)
                .build();

        var produtoAtualizadoRequest = AtualizarProdutoRequest.builder()
                .nome("Caneta Preta Atualizada")
                .valorDoProduto(new BigDecimal(3.5))
                .build();

        when(repository.getById(any())).thenReturn(produto);

        var produtoAtualizado = service.atualizar(1, produtoAtualizadoRequest);

        assertEquals(produtoAtualizado.getNomeDoProduto(), produtoAtualizado.getNomeDoProduto());

        verify(repository, times(1)).getById(produto.getId());
    }

    @Test
    public void atualizar_produto_NaoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Produto não cadastrado!")).when(repository).getById(1);

        assertThatThrownBy(() -> service.atualizar(1, any()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Produto não cadastrado!");

        verify(repository, times(1)).getById(1);
    }

    @Test
    public void remover_produto_sucesso() throws Exception {

        var produto = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Preta")
                .valorDoProduto(new BigDecimal(3.0))
                .build();

        service.remover(1);

        verify(repository, times(1)).deleteById(produto.getId());
    }

    @Test
    public void remover_produto_NaoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Produto não cadastrado!")).when(repository).deleteById(any());

        assertThatThrownBy(() -> service.remover(any()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Produto não cadastrado!");
    }
}
