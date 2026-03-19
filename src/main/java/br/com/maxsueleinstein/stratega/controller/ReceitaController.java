package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.dto.ReceitaRequestDTO;
import br.com.maxsueleinstein.stratega.domain.model.Receita;
import br.com.maxsueleinstein.stratega.service.ReceitaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/receita")
public class ReceitaController {

    private final ReceitaService service;

    public ReceitaController(ReceitaService service) {
        this.service = service;
    }
    
    @GetMapping("{id}")
    public Optional<Receita> obterReceitaPorId(@PathVariable Long id){
        return service.obterReceita(id);
    }
    
    @GetMapping
    public List<Receita> obterReceitas(){
        return service.obterReceitas();
    }

    @PostMapping
    public Receita criarReceita(@RequestBody ReceitaRequestDTO receita){
        return service.criarReceita(receita);
    }


}