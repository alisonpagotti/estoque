package br.com.xbrain.estoque.modulos.estoque.service;

import br.com.xbrain.estoque.modulos.comum.service.DataHoraService;
import br.com.xbrain.estoque.modulos.estoque.dto.AtualizarEstoqueRequest;
import br.com.xbrain.estoque.modulos.estoque.dto.EstoqueRequest;
import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.estoque.repository.EstoqueRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EstoqueServiceTest {

    @Mock
    private EstoqueRepository repository;

    @Mock
    private DataHoraService dataHoraService;

    @InjectMocks
    private EstoqueService service;

    @Test
    public void detalhar_porEstoque_sucesso() throws Exception {

        var estoque = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Caneta")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .build();

        when(repository.getById(1)).thenReturn(estoque);

        var estoqueDetalhado = service.detalhar(estoque.getId());

        assertEquals(estoque.getId(), estoqueDetalhado.getId());

        verify(repository, times(1)).getById(estoque.getId());
    }

    @Test
    public void detalhar_porEstoque_naoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Estoque não cadastrado!")).when(repository).getById(1);

        assertThatThrownBy(() -> service.detalhar(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Estoque não cadastrado!");

        verify(repository, times(1)).getById(1);
    }

    @Test
    public void cadastrar_estoque_sucesso() throws Exception {

        var dataAtual = LocalDateTime.now();

        var estoque = Estoque.builder()
                .nomeDoEstoque("Estoque de Caneta")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .dataCadastro(dataAtual)
                .build();

        var estoqueRequest = EstoqueRequest.builder()
                .nomeDoEstoque("Estoque de Caneta")
                .build();

        when(dataHoraService.DataHoraAtual()).thenReturn(dataAtual);

        var estoqueCadastrado = service.cadastrar(estoqueRequest);

        assertEquals(estoque.getNomeDoEstoque(), estoqueCadastrado.getNomeDoEstoque());
        verify(repository, times(1)).save(estoque);
    }

    @Test
    public void atualizar_estoque_sucesso() throws Exception {

        var estoque = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Caneta Atualizada")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .build();

        var estoqueAtualizadoRequest = AtualizarEstoqueRequest.builder()
                .nome("Estoque de Caneta Atualizada")
                .build();

        when(repository.getById(1)).thenReturn(estoque);

        var estoqueAtualizado = service.atualizar(1, estoqueAtualizadoRequest);

        assertEquals(estoque.getNomeDoEstoque(), estoqueAtualizado.getNomeDoEstoque());

        verify(repository, times(1)).getById(estoque.getId());
    }

    @Test
    public void atualizar_estoque_notFound() throws Exception {

        assertThatThrownBy(() -> service.atualizar(any(), any()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Estoque não cadastrado!");
    }

    @Test
    public void remover_estoque_sucesso() throws Exception {

        var estoque = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Caneta Atualizada")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .build();

        service.remover(1);

        verify(repository, times(1)).deleteById(estoque.getId());
    }

    @Test
    public void remover_estoque_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Estoque não cadastrado!")).when(repository).deleteById(any());

        assertThatThrownBy(() -> service.remover(any()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Estoque não cadastrado!");
    }
}
