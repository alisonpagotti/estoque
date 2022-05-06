package br.com.xbrain.estoque.modulos.produto.controller;

import br.com.xbrain.estoque.modulos.produto.dto.AtualizarProdutoRequest;
import br.com.xbrain.estoque.modulos.produto.dto.ProdutoRequest;
import br.com.xbrain.estoque.modulos.produto.dto.ProdutoResponse;
import br.com.xbrain.estoque.modulos.produto.service.ProdutoService;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping("listar")
    public List<ProdutoResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("detalhar/{id}")
    public ProdutoResponse detalhar(@PathVariable Integer id) {
        return service.detalhar(id);
    }

    @PostMapping
    public ProdutoResponse cadastrar(@RequestBody @Valid ProdutoRequest request) {
        return service.cadastrar(request);
    }

    @PutMapping("{id}")
    public ProdutoResponse atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarProdutoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("{id}")
    public void remover(@PathVariable Integer id) {
        service.remover(id);
    }
}
