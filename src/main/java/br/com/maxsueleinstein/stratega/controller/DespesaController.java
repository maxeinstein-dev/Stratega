package br.com.maxsueleinstein.stratega.controller;

import br.com.maxsueleinstein.stratega.domain.dto.DespesaRequestDTO;
import br.com.maxsueleinstein.stratega.domain.model.Despesa;
import br.com.maxsueleinstein.stratega.service.DespesaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/despesa")
public class DespesaController {

    private final DespesaService service;

    public DespesaController(DespesaService service) {
        this.service = service;
    }
    
    @GetMapping("{id}")
    public Optional<Despesa> obterDespesaPorId(@PathVariable Long id){
        return service.obterDespesa(id);
    }
    
    @GetMapping
    public List<Despesa> obterDespesas(){
        return service.obterDespesas();
    }

    @PostMapping
    public Despesa criarDespesa(@RequestBody DespesaRequestDTO despesa){
        return service.criarDespesa(despesa);
    }


}