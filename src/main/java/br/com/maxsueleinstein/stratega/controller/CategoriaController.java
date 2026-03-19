package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.model.Categoria;
import br.com.maxsueleinstein.stratega.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Categoria> listarCategorias(@RequestParam(required = false) String nome){
        if(nome != null && !nome.isBlank()){
            return service.obterCategoriaPorNome(nome);
        }
        return service.obterCategorias();
    }

    @GetMapping("{id}")
    public Optional<Categoria> obterCategoriaPorId(@PathVariable Long id){
        return service.obterCategoria(id);
    }

    @PostMapping
    public Categoria criarCategoria(@RequestBody Categoria categoria){
        return service.criarCategoria(categoria);
    }
}