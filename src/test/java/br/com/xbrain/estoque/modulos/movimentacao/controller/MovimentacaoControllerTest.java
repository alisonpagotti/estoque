package br.com.xbrain.estoque.modulos.movimentacao.controller;

import br.com.xbrain.estoque.modulos.movimentacao.dto.MovimentacaoResponse;
import br.com.xbrain.estoque.modulos.movimentacao.enums.ETipo;
import br.com.xbrain.estoque.modulos.movimentacao.model.Movimentacao;
import br.com.xbrain.estoque.modulos.movimentacao.service.MovimentacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static br.com.xbrain.estoque.modulos.movimentacao.MovimentacaoHelper.umEstoque;
import static br.com.xbrain.estoque.modulos.movimentacao.MovimentacaoHelper.umProduto;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovimentacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimentacaoService service;

    @Test
    public void litar_todasMovimentacoes_sucesso() throws Exception {

        List<Movimentacao> listaMovimentacoes = Arrays.asList(
                Movimentacao.builder()
                        .id(1)
                        .tipo(ETipo.ENTRADA)
                        .produto(umProduto(1, "Caneta Preta"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Entrada de 10 Canetas Pretas")
                        .build(),

                Movimentacao.builder()
                        .id(2)
                        .tipo(ETipo.ENTRADA)
                        .produto(umProduto(2, "Caneta Azul"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Entrada de 10 Canetas Azuis")
                        .build()
        );

        when(service.listarTodas()).thenReturn(MovimentacaoResponse.of(listaMovimentacoes));

        mockMvc.perform(get("/movimentacoes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.[0].quantidade").value(10))
                .andExpect(jsonPath("$.[0].nomeProduto").value("Caneta Preta"))
                .andExpect(jsonPath("$.[0].nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.[0].observacao").value("Entrada de 10 Canetas Pretas"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.[1].quantidade").value(10))
                .andExpect(jsonPath("$.[1].nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.[1].nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.[1].observacao").value("Entrada de 10 Canetas Azuis"));
    }

    @Test
    public void listar_nenhum_sucesso() throws Exception {

        when(service.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/movimentacoes/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void listar_porTipo_entrada_sucesso() throws Exception {

        List<Movimentacao> listaMovimentacoes = Arrays.asList(
                Movimentacao.builder()
                        .id(1)
                        .tipo(ETipo.ENTRADA)
                        .produto(umProduto(1, "Caneta Preta"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Entrada de 10 Canetas Pretas")
                        .build(),

                Movimentacao.builder()
                        .id(2)
                        .tipo(ETipo.ENTRADA)
                        .produto(umProduto(2, "Caneta Azul"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Entrada de 10 Canetas Azuis")
                        .build()
        );

        when(service.listarPorTipoMovimentacao(any())).thenReturn(MovimentacaoResponse.of(listaMovimentacoes));

        mockMvc.perform(get("/movimentacoes/listar/{tipo}", "entrada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.[0].quantidade").value(10))
                .andExpect(jsonPath("$.[0].nomeProduto").value("Caneta Preta"))
                .andExpect(jsonPath("$.[0].nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.[0].observacao").value("Entrada de 10 Canetas Pretas"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.[1].quantidade").value(10))
                .andExpect(jsonPath("$.[1].nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.[1].nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.[1].observacao").value("Entrada de 10 Canetas Azuis"));
    }

    @Test
    public void listar_porTipo_saida_sucesso() throws Exception {

        List<Movimentacao> listaMovimentacoes = Arrays.asList(
                Movimentacao.builder()
                        .id(1)
                        .tipo(ETipo.SAIDA)
                        .produto(umProduto(1, "Caneta Preta"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Saída de 10 Canetas Pretas")
                        .build(),

                Movimentacao.builder()
                        .id(2)
                        .tipo(ETipo.SAIDA)
                        .produto(umProduto(2, "Caneta Azul"))
                        .quantidade(10)
                        .estoque(umEstoque(1, "Estoque de Canetas"))
                        .observacao("Saída de 10 Canetas Azuis")
                        .build()
        );

        when(service.listarPorTipoMovimentacao(any())).thenReturn(MovimentacaoResponse.of(listaMovimentacoes));

        mockMvc.perform(get("/movimentacoes/listar/{tipo}", "saida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].tipo").value("SAIDA"))
                .andExpect(jsonPath("$.[0].quantidade").value(10))
                .andExpect(jsonPath("$.[0].nomeProduto").value("Caneta Preta"))
                .andExpect(jsonPath("$.[0].nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.[0].observacao").value("Saída de 10 Canetas Pretas"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].tipo").value("SAIDA"))
                .andExpect(jsonPath("$.[1].quantidade").value(10))
                .andExpect(jsonPath("$.[1].nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.[1].nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.[1].observacao").value("Saída de 10 Canetas Azuis"));
    }

    @Test
    public void listar_porTipo_naoCadastrado_badRequest() throws Exception {

        doThrow(new IllegalArgumentException("Tipo de movimentação não cadastrada!"))
                .when(service).listarPorTipoMovimentacao(any());

        mockMvc.perform(get("/movimentacoes/listar/{tipo}", "espera"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Tipo de movimentação não cadastrada!"));
    }

    @Test
    public void detalhar_porMovimentacao_sucesso() throws Exception {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.ENTRADA)
                .produto(umProduto(1, "Caneta Azul"))
                .quantidade(10)
                .estoque(umEstoque(1, "Estoque de Canetas"))
                .observacao("Entrada de 10 Canetas Azuis")
                .build();

        when(service.detalhar(any())).thenReturn(MovimentacaoResponse.of(movimentacao));

        mockMvc.perform(get("/movimentacoes/detalhar/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.observacao").value("Entrada de 10 Canetas Azuis"));
    }

    @Test
    public void detalhar_porMovimentacao_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Movimentação não cadastrada!")).when(service).detalhar(1);

        mockMvc.perform(get("/movimentacoes/detalhar/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Movimentação não cadastrada!"));
    }

    @Test
    public void movimentacao_entrada_sucesso() throws Exception {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.ENTRADA)
                .produto(umProduto(1, "Caneta Azul"))
                .quantidade(10)
                .estoque(umEstoque(1, "Estoque de Canetas"))
                .observacao("Entrada de 10 Canetas Azuis")
                .build();

        when(service.movimentacao(any())).thenReturn(MovimentacaoResponse.of(movimentacao));

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"ENTRADA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Entrada de 10 Canetas Azuis\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.observacao").value("Entrada de 10 Canetas Azuis"));
    }

    @Test
    public void movimentacao_saida_sucesso() throws Exception {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.SAIDA)
                .produto(umProduto(1, "Caneta Azul"))
                .quantidade(10)
                .estoque(umEstoque(1, "Estoque de Canetas"))
                .observacao("Saída de 10 Canetas Azuis")
                .build();

        when(service.movimentacao(any())).thenReturn(MovimentacaoResponse.of(movimentacao));

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"SAIDA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Saída de 10 Canetas Azuis\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("SAIDA"))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.observacao").value("Saída de 10 Canetas Azuis"));
    }

    @Test
    public void movimentacao_entrada_idProdutoNull_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"ENTRADA\", " +
                                "\"idProduto\": , " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Entrada de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void movimentacao_saida_idProdutoNull_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"SAIDA\", " +
                                "\"idProduto\": , " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Saída de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void movimentacao_entrada_idEstoqueNull_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"ENTRADA\", " +
                                "\"idProduto\" 1: , " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": , " +
                                "\"observacao\": \"Entrada de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void movimentacao_saida_idEstoqueNull_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"SAIDA\", " +
                                "\"idProduto\" 1: , " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": , " +
                                "\"observacao\": \"Saída de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void movimentacao_entrada_quantidadeNull_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"ENTRADA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": , " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Entrada de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void movimentacao_saida_quantidadeNull_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"SAIDA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": , " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Saída de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void movimentacao_entrada_quantidadeZero_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"ENTRADA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": 0, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Entrada de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").value("O valor mínimo deve ser maior do que zero!"));
    }

    @Test
    public void movimentacao_saida_quantidadeZero_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"SAIDA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": 0, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Saída de 10 Canetas Azuis\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").value("O valor mínimo deve ser maior do que zero!"));
    }

    @Test
    public void movimentacao_entrada_observacaoMaiorQueCemCaracteres_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"ENTRADA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Esse teste não pode passar de 100 caracteres, " +
                                "pois não permite essa quantidade, deve ser com menos caracteres!\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").value("O máximo permitido são 100 caracteres!"));
    }

    @Test
    public void movimentacao_saida_observacaoMaiorQueCemCaracteres_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(post("/movimentacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, " +
                                "\"tipo\": \"SAIDA\", " +
                                "\"idProduto\": 1, " +
                                "\"quantidade\": 10, " +
                                "\"idEstoque\": 1, " +
                                "\"observacao\": \"Esse teste não pode passar de 100 caracteres, " +
                                "pois não permite essa quantidade, deve ser com menos caracteres!\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").value("O máximo permitido são 100 caracteres!"));
    }

    @Test
    public void atualizar_entrada_movimentacao_sucesso() throws Exception {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.ENTRADA)
                .produto(umProduto(1, "Caneta Azul"))
                .quantidade(10)
                .estoque(umEstoque(1, "Estoque de Canetas"))
                .observacao("Entrada de 10 Canetas Azuis atualizada")
                .build();

        when(service.atualizar(any(), any())).thenReturn(MovimentacaoResponse.of(movimentacao));

        mockMvc.perform(put("/movimentacoes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observacao\": \"Entrada de 10 Canetas Azuis atualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("ENTRADA"))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.observacao").value("Entrada de 10 Canetas Azuis atualizada"));
    }

    @Test
    public void atualizar_saida_movimentacao_sucesso() throws Exception {

        var movimentacao = Movimentacao.builder()
                .id(1)
                .tipo(ETipo.SAIDA)
                .produto(umProduto(1, "Caneta Azul"))
                .quantidade(10)
                .estoque(umEstoque(1, "Estoque de Canetas"))
                .observacao("Saída de 10 Canetas Azuis atualizada")
                .build();

        when(service.atualizar(any(), any())).thenReturn(MovimentacaoResponse.of(movimentacao));

        mockMvc.perform(put("/movimentacoes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observacao\": \"Saída de 10 Canetas Azuis atualizada\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("SAIDA"))
                .andExpect(jsonPath("$.quantidade").value(10))
                .andExpect(jsonPath("$.nomeProduto").value("Caneta Azul"))
                .andExpect(jsonPath("$.nomeEstoque").value("Estoque de Canetas"))
                .andExpect(jsonPath("$.observacao").value("Saída de 10 Canetas Azuis atualizada"));
    }

    @Test
    public void atualizar_movimentacao_observacaoMaiorQueCemCaracteres_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).movimentacao(any());

        mockMvc.perform(put("/movimentacoes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observacao\": \"Esse teste não pode passar de 100 caracteres, " +
                                "pois não permite essa quantidade, deve ser com menos caracteres!\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").value("O máximo permitido são 100 caracteres!"));
    }

    @Test
    public void atualizar_movimentacao_naoCadastrada_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Movimentação não cadastrada!")).when(service).atualizar(any(), any());

        mockMvc.perform(put("/movimentacoes/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"observacao\": \"Entrada atualizada!\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detalhes").value("Movimentação não cadastrada!"));
    }
}