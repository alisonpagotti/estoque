package br.com.xbrain.estoque.modulos.produto.controller;

import br.com.xbrain.estoque.modulos.produto.dto.ProdutoResponse;
import br.com.xbrain.estoque.modulos.produto.model.Produto;
import br.com.xbrain.estoque.modulos.produto.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;

    @Test
    public void listar_todosProdutos_sucesso() throws Exception {

        List<Produto> listaProdutos = Arrays.asList(
                Produto.builder()
                        .id(1)
                        .nomeDoProduto("Caneta Preta")
                        .quantidade(0)
                        .valorDoProduto(new BigDecimal(2.5))
                        .build(),

                Produto.builder()
                        .id(2)
                        .nomeDoProduto("Caderno com 100 folhas")
                        .quantidade(0)
                        .valorDoProduto(new BigDecimal(20.0))
                        .build()
        );

        when(service.listarTodos()).thenReturn(ProdutoResponse.of(listaProdutos));

        mockMvc.perform(get("/produtos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].nome").value("Caneta Preta"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].nome").value("Caderno com 100 folhas"));
    }

    @Test
    public void listar_nenhum_sucesso() throws Exception {

        when(service.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/produtos/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void detalhar_porProduto_sucesso() throws Exception {

        var produto = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Preta")
                .valorDoProduto(new BigDecimal(3.0))
                .build();

        when(service.detalhar(any())).thenReturn(ProdutoResponse.of(produto));

        mockMvc.perform(get("/produtos/detalhar/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Caneta Preta"));
    }

    @Test
    public void detalhar_porProduto_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Produto não cadastrado!")).when(service).detalhar(1);

        mockMvc.perform(get("/produtos/detalhar/{id}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Produto não cadastrado!"));
    }

    @Test
    public void cadastrar_produto_sucesso() throws Exception {

        var produto = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Preta")
                .valorDoProduto(new BigDecimal(3.0))
                .build();

        when(service.cadastrar(any())).thenReturn(ProdutoResponse.of(produto));

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"Caneta Preta\", \"valorDoProduto\": 3.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Caneta Preta"));
    }

    @Test
    public void cadastrar_produto_semNome_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).cadastrar(any());

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"\", \"valorDoProduto\": 3.0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void cadastrar_produto_comNomeJaCadastrado_dataIntegrityViolation() throws Exception {

        doThrow(new DataIntegrityViolationException("Produto já cadastrado!")).when(service).cadastrar(any());

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"Caneta Preta\", \"valorDoProduto\": 3.5}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Produto já cadastrado!"));
    }

    @Test
    public void atualizar_produto_sucesso() throws Exception {

        var produto = Produto.builder()
                .id(1)
                .nomeDoProduto("Caneta Preta Atualizada")
                .valorDoProduto(new BigDecimal(3.5))
                .build();

        when(service.atualizar(any(), any())).thenReturn(ProdutoResponse.of(produto));

        mockMvc.perform(put("/produtos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"Caneta Preta Atualizada\", \"valorDoProduto\": 3.5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Caneta Preta Atualizada"));
    }

    @Test
    public void atualizar_produto_semNome_badRequest() throws Exception {

        doThrow(HttpClientErrorException.BadRequest.class).when(service).atualizar(any(), any());

        mockMvc.perform(put("/produtos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nome\": \"\", \"valorDoProduto\": 3.5}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void atualizar_produto_semCadastro_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Produto não cadastrado!")).when(service).atualizar(any(), any());

        mockMvc.perform(put("/produtos/{id}", 5L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\": \"Caneta Preta Atualizada\", \"valorDoProduto\": 3.5}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Produto não cadastrado!"));
    }

    @Test
    public void remover_produto_sucesso() throws Exception {

        doNothing().when(service).remover(any());

        mockMvc.perform(delete("/produtos/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    public void remover_produto_naoCadastrado_notFound() throws Exception {

        doThrow(new EntityNotFoundException("Produto não cadastrado!")).when(service).remover(any());

        mockMvc.perform(delete("/produtos/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Não foi possível realizar essa operação!"))
                .andExpect(jsonPath("$.detalhes").value("Produto não cadastrado!"));
    }
}
