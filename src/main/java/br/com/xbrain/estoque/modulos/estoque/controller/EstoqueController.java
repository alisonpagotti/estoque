package br.com.xbrain.estoque.modulos.estoque.controller;

import br.com.xbrain.estoque.modulos.estoque.dto.AtualizarEstoqueRequest;
import br.com.xbrain.estoque.modulos.estoque.dto.EstoqueRequest;
import br.com.xbrain.estoque.modulos.estoque.dto.EstoqueResponse;
import br.com.xbrain.estoque.modulos.estoque.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("estoques")
public class EstoqueController {

    @Autowired
    private EstoqueService service;

    @GetMapping("listar")
    public List<EstoqueResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("detalhar/{id}")
    public EstoqueResponse detalhar(@PathVariable Integer id) {
        return service.detalhar(id);
    }

    @PostMapping
    public EstoqueResponse cadastrar(@RequestBody @Valid EstoqueRequest request) {
        return service.cadastrar(request);
    }

    @PutMapping("{id}")
    public EstoqueResponse atualizar(@PathVariable Integer id, @RequestBody @Valid AtualizarEstoqueRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("{id}")
    public void remover(@PathVariable Integer id) {
        service.remover(id);
    }
}
