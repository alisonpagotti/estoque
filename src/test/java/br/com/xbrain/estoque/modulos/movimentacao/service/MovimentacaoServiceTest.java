package br.com.xbrain.estoque.modulos.movimentacao.service;

import br.com.xbrain.estoque.modulos.comum.service.DataHoraService;
import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.estoque.repository.EstoqueRepository;
import br.com.xbrain.estoque.modulos.movimentacao.dto.AtualizarMovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoRequest;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import br.com.xbrain.estoque.modulos.movimentacao.repository.MovimentacaoRepository;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import br.com.xbrain.estoque.modulos.produto.repository.ProdutoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.xbrain.estoque.modulos.movimentacao.MovimentacaoHelper.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MovimentacaoServiceTest {

    @Mock
    private MovimentacaoRepository repository;
    @Mock
    private EstoqueRepository estoqueRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private DataHoraService dataHoraService;
    @InjectMocks
    private MovimentacaoService service;

    @Test
    public void listar_porTipo_entrada_sucesso() {

        var listaMovimentacoes = umaListaMovimentacaoEntrada();

        when(repository.findByTipo(ETipo.ENTRADA)).thenReturn(listaMovimentacoes);

        var listaTipoEntrada = service.listarPorTipoMovimentacao("entrada");

        assertEquals(listaMovimentacoes.size(), listaTipoEntrada.size());
        assertEquals(listaTipoEntrada.get(0).getId(), 1);
        assertEquals(listaTipoEntrada.get(0).getNomeProduto(), "Caneta Preta");
        assertEquals(listaTipoEntrada.get(1).getId(), 2);
        assertEquals(listaTipoEntrada.get(1).getNomeProduto(), "Caneta Azul");

        verify(repository, times(1)).findByTipo(ETipo.ENTRADA);
    }

    @Test
    public void listar_porTipo_saida_sucesso() {

        var listaMovimentacoes = umaListaMovimentacaoSaida();

        when(repository.findByTipo(ETipo.SAIDA)).thenReturn(listaMovimentacoes);

        var listaTipoEntrada = service.listarPorTipoMovimentacao("saida");

        assertEquals(listaMovimentacoes.size(), listaTipoEntrada.size());
        assertEquals(listaTipoEntrada.get(0).getId(), 1);
        assertEquals(listaTipoEntrada.get(0).getNomeProduto(), "Caneta Preta");
        assertEquals(listaTipoEntrada.get(1).getId(), 2);
        assertEquals(listaTipoEntrada.get(1).getNomeProduto(), "Caneta Azul");

        verify(repository, times(1)).findByTipo(ETipo.SAIDA);
    }

    @Test
    public void listar_porTipo_naoCadastrado_badRequest() {

        assertThatThrownBy(() -> service.listarPorTipoMovimentacao("espera"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tipo de movimenta????o n??o cadastrada!");
    }

    @Test
    public void listar_porPeriodo_sucesso() {

        var periodoInicio = LocalDateTime.of(
                2022, 5, 10,
                9, 0, 0);

        var periodoFim = LocalDateTime.of(
                2022, 5, 30,
                9, 0, 0);

        var listaMovimentacoes = umaListaMovimentacaoEntrada();

        when(repository.findByDataCadastroBetween(periodoInicio,periodoFim)).thenReturn(listaMovimentacoes);

        var listaPorPeriodo = service.listarPorPeriodo(periodoInicio, periodoFim);

        assertEquals(listaMovimentacoes.size(), listaPorPeriodo.size());
        assertEquals(listaPorPeriodo.get(0).getId(), 1);
        assertEquals(listaPorPeriodo.get(0).getNomeProduto(), "Caneta Preta");
        assertEquals(listaPorPeriodo.get(1).getId(), 2);
        assertEquals(listaPorPeriodo.get(1).getNomeProduto(), "Caneta Azul");

        verify(repository, times(1)).findByDataCadastroBetween(periodoInicio, periodoFim);
    }

    @Test
    public void detalhar_porMovimentacao_sucesso() {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.ENTRADA)
                .produto(umProduto(1, "Caneta Azul"))
                .quantidade(10)
                .estoque(umEstoque(1, "Estoque de Canetas"))
                .observacao("Entrada da 10 canetas azuis")
                .build();

        when(repository.getById(1)).thenReturn(movimentacao);

        var movimentacaoDetalhada = service.detalhar(movimentacao.getId());

        assertEquals(movimentacao.getId(), movimentacaoDetalhada.getId());
        assertEquals(movimentacaoDetalhada.getNomeProduto(), "Caneta Azul");

        verify(repository, times(1)).getById(movimentacao.getId());
    }

    @Test
    public void detalhar_porMovimentacao_naoCadastrada_notFound() {

        doThrow(new EntityNotFoundException("Movimentacao n??o cadastrada!")).when(repository).getById(1);

        assertThatThrownBy(() -> service.detalhar(1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Movimenta????o n??o cadastrada!");

        verify(repository, times(1)).getById(1);
    }

    @Test
    public void movimentacao_entrada_sucesso() {

        var dataAtual = LocalDateTime.now();

        when(produtoRepository.getById(any())).thenReturn(umProdutoDataCadastro(1, "Caneta Azul", dataAtual));
        when(estoqueRepository.getById(any())).thenReturn(umEstoqueDataCadastro(1, "Estoque de Canetas", dataAtual));
        when(dataHoraService.DataHoraAtual()).thenReturn(dataAtual);

        var movimentacaoRequest = MovimentacaoRequest.builder()
                .tipo(ETipo.ENTRADA)
                .idProduto(umProdutoDataCadastro(1, "Caneta Azul", dataAtual).getId())
                .idEstoque(umEstoqueDataCadastro(1, "Estoque de Canetas", dataAtual).getId())
                .quantidade(10)
                .observacao("Entrada de 10 canetas azuis")
                .build();

        var estoqueAtual = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Canetas")
                .quantidadeTotal(10)
                .valorTotal(new BigDecimal(30.0))
                .produto(new ArrayList<>())
                .dataCadastro(dataAtual)
                .build();

        var produtoAtual = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Azul")
                .valorDoProduto(new BigDecimal(3.0))
                .quantidade(movimentacaoRequest.getQuantidade())
                .estoque(estoqueAtual)
                .dataCadastro(dataAtual)
                .build();

        var movimentacao = Movimentacao.builder()
                .tipo(ETipo.ENTRADA)
                .quantidade(10)
                .produto(produtoAtual)
                .estoque(estoqueAtual)
                .observacao("Entrada de 10 canetas azuis")
                .dataCadastro(dataAtual)
                .build();

        var movimentacaoEntrada = service.movimentacao(movimentacaoRequest);

        assertEquals(movimentacao.getTipo(), movimentacaoEntrada.getTipo());
        assertEquals(movimentacaoEntrada.getNomeProduto(), "Caneta Azul");

        verify(repository, times(1)).save(movimentacao);
    }

    @Test
    public void movimentacao_saida_sucesso() {

        var dataAtual = LocalDateTime.now();

        var estoqueAtual = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Canetas")
                .quantidadeTotal(10)
                .valorTotal(new BigDecimal(30.0))
                .produto(new ArrayList<>())
                .dataCadastro(dataAtual)
                .build();

        var produtoAtual = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Azul")
                .valorDoProduto(new BigDecimal(3.0))
                .quantidade(10)
                .estoque(estoqueAtual)
                .dataCadastro(dataAtual)
                .build();

        var movimentacao = Movimentacao.builder()
                .tipo(ETipo.SAIDA)
                .quantidade(5)
                .produto(produtoAtual)
                .estoque(estoqueAtual)
                .observacao("Sa??da de 5 canetas azuis")
                .dataCadastro(dataAtual)
                .build();

        when(estoqueRepository.getById(any())).thenReturn(estoqueAtual);
        when(produtoRepository.getById(any())).thenReturn(produtoAtual);
        when(dataHoraService.DataHoraAtual()).thenReturn(dataAtual);

        var movimentacaoRequest = MovimentacaoRequest.builder()
                .tipo(ETipo.SAIDA)
                .idProduto(produtoAtual.getId())
                .idEstoque(estoqueAtual.getId())
                .quantidade(5)
                .observacao("Sa??da de 5 canetas azuis")
                .build();

        var movimentacaoSaida = service.movimentacao(movimentacaoRequest);

        assertEquals(movimentacao.getTipo(), movimentacaoSaida.getTipo());
        assertEquals(movimentacaoSaida.getNomeProduto(), "Caneta Azul");

        verify(repository, times(1)).save(movimentacao);
    }

    @Test
    public void movimentacao_entrada_maiorQueCemUnidades_badRequest() {

        when(produtoRepository.getById(any())).thenReturn(umProduto(1, "Caneta Azul"));
        when(estoqueRepository.getById(any())).thenReturn(umEstoque(1, "Estoque de Canetas"));

        var movimentacaoRequest = MovimentacaoRequest.builder()
                .tipo(ETipo.ENTRADA)
                .idProduto(umProduto(1, "Caneta Azul").getId())
                .idEstoque(umEstoque(1, "Estoque de Canetas").getId())
                .quantidade(110)
                .observacao("Entrada de 110 canetas azuis")
                .build();

        assertThatThrownBy(() -> service.movimentacao(movimentacaoRequest))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Quantidade ultrapassa o limite de 100 unidades do estoque! " +
                        "O estoque ainda permite a entrada de 100 unidades.");
    }

    @Test
    public void movimentacao_saida_maiorQueQuantidadeEmEstoque_badRequest() {

        when(produtoRepository.getById(any())).thenReturn(umProduto(1, "Caneta Azul"));
        when(estoqueRepository.getById(any())).thenReturn(umEstoque(1, "Estoque de Canetas"));

        var movimentacaoRequest = MovimentacaoRequest.builder()
                .tipo(ETipo.SAIDA)
                .idProduto(umProduto(1, "Caneta Azul").getId())
                .idEstoque(umEstoque(1, "Estoque de Canetas").getId())
                .quantidade(10)
                .observacao("Sa??da de 10 canetas azuis")
                .build();

        assertThatThrownBy(() -> service.movimentacao(movimentacaoRequest))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Quantidade de sa??da maior do que a quantidade que o estoque possui!");
    }

    @Test
    public void movimentacao_saida_maiorQueQuantidadeProduto_badRequest() {

        when(produtoRepository.getById(any())).thenReturn(umProdutoQuantidade(1, "Caneta Azul", 10));
        when(estoqueRepository.getById(any())).thenReturn(umEstoqueQuantidade(1, "Estoque de Canetas", 20));

        var movimentacaoRequest = MovimentacaoRequest.builder()
                .tipo(ETipo.SAIDA)
                .idProduto(umProdutoQuantidade(1, "Caneta Azul", 10).getId())
                .idEstoque(umEstoqueQuantidade(1, "Estoque de Canetas", 20).getId())
                .quantidade(15)
                .observacao("Sa??da de 15 canetas azuis")
                .build();

        assertThatThrownBy(() -> service.movimentacao(movimentacaoRequest))
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Quantidade de sa??da maior do que a quantidade que o estoque possui!");
    }

    @Test
    public void atualizar_movimentacao_sucesso() {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.ENTRADA)
                .quantidade(10)
                .produto(umProduto(1, "Caneta Azul"))
                .estoque(umEstoque(1, "Estoque de Caneta"))
                .observacao("Sa??da de 10 canetas azuis")
                .build();

        var atualizarMovimentacaoRequest = AtualizarMovimentacaoRequest.builder()
                .observacao("Observa????o atualizada.")
                .build();

        when(repository.getById(any())).thenReturn(movimentacao);

        var movimentacaoAtualizada = service.atualizar(1, atualizarMovimentacaoRequest);

        assertEquals(movimentacao.getObservacao(), movimentacaoAtualizada.getObservacao());

        verify(repository, times(1)).getById(movimentacao.getId());
    }

    @Test
    public void atualizar_movimentacao_naoCadastrada_notFound() {

        doThrow(new EntityNotFoundException("Movimenta????o n??o cadastrada!"))
                .when(repository).getById(any());

        assertThatThrownBy(() -> service.atualizar(1, any()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Movimenta????o n??o cadastrada!");

        verify(repository, times(1)).getById(1);
    }
}