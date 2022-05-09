package br.com.xbrain.estoque.modulos.estoque.controller;

import br.com.xbrain.estoque.modulos.estoque.dto.EstoqueResponse;
import br.com.xbrain.estoque.modulos.estoque.model.Estoque;
import br.com.xbrain.estoque.modulos.estoque.service.EstoqueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstoqueService service;

    @Test
    public void listar_todosEstoques_sucesso() throws Exception {

        List<Estoque> listaEstoque = Arrays.asList(
                Estoque.builder()
                        .id(1)
                        .nomeDoEstoque("Estoque de Caneta")
                        .quantidadeTotal(0)
                        .produto(new ArrayList<>())
                        .valorTotal(new BigDecimal(0.0))
                        .build(),

                Estoque.builder()
                        .id(2)
                        .nomeDoEstoque("Estoque de Caderno")
                        .quantidadeTotal(0)
                        .produto(new ArrayList<>())
                        .valorTotal(new BigDecimal(0.0))
                        .build()
        );

        when(service.listarTodos()).thenReturn(EstoqueResponse.of(listaEstoque));

        mockMvc.perform(get("/estoques/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].nomeDoEstoque").value("Estoque de Caneta"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].nomeDoEstoque").value("Estoque de Caderno"));
    }

    @Test
    public void listar_nenhum_sucesso() throws Exception {

        when(service.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/estoques/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void detalhar_porEstoque_sucesso() throws Exception {
        var estoque = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Caneta")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .build();

        when(service.detalhar(any())).thenReturn(EstoqueResponse.of(estoque));

        mockMvc.perform(get("/estoques/detalhar/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeDoEstoque").value("Estoque de Caneta"));
    }

    @Test
    public void detalhar_porEstoque_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Estoque não cadastrado!")).when(service).detalhar(1);

        mockMvc.perform(get("/estoques/detalhar/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Estoque não cadastrado!"));
    }

    @Test
    public void cadastrar_estoque_sucesso() throws Exception {

        var estoque = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Caneta")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .build();

        when(service.cadastrar(any())).thenReturn(EstoqueResponse.of(estoque));

        mockMvc.perform(post("/estoques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nomeDoEstoque\": \"Estoque de Caneta\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeDoEstoque").value("Estoque de Caneta"));
    }

    @Test
    public void cadastrar_estoque_semNome_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).cadastrar(any());

        mockMvc.perform(post("/estoques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void atualizar_estoque_sucesso() throws Exception {

        var estoqueAtualizado = Estoque.builder()
                .id(1)
                .nomeDoEstoque("Estoque de Caneta Atualizado")
                .quantidadeTotal(0)
                .produto(new ArrayList<>())
                .valorTotal(new BigDecimal(0.0))
                .build();

        when(service.atualizar(any(), any())).thenReturn(EstoqueResponse.of(estoqueAtualizado));

        mockMvc.perform(put("/estoques/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"Estoque de Caneta Atualizado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nomeDoEstoque").value("Estoque de Caneta Atualizado"));
    }

    @Test
    public void atualizar_estoque_semNome_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).atualizar(any(), any());

        mockMvc.perform(put("/estoques/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void atualizar_estoque_naoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Estoque não cadastrado!")).when(service).atualizar(any(), any());

        mockMvc.perform(put("/estoques/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Estoque de Caneta Atualizada\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Estoque não cadastrado!"));
    }

    @Test
    public void remover_estoque_sucesso() throws Exception {

        doNothing().when(service).remover(any());

        mockMvc.perform(delete("/estoques/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void remover_estoque_naoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Estoque não cadastrado!")).when(service).remover(any());

        mockMvc.perform(delete("/estoques/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Estoque não cadastrado!"));
    }
}
