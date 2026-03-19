package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.dto.DespesaRequestDTO;
import br.com.maxsueleinstein.stratega.domain.model.Categoria;
import br.com.maxsueleinstein.stratega.domain.model.Despesa;
import br.com.maxsueleinstein.stratega.repository.CategoriaRepository;
import br.com.maxsueleinstein.stratega.repository.DespesaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {
    private final DespesaRepository repository;
    private final CategoriaRepository categoriaRepository;

    public DespesaService(DespesaRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public Despesa criarDespesa (DespesaRequestDTO dto){
        Despesa novaDespesa = new Despesa();
        novaDespesa.setNome(dto.nome());
        novaDespesa.setValorPrevisto(dto.valorPrevisto());

        if(dto.categoriaId() != null){
            novaDespesa.setCategoria(categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada")));
        } else if (dto.categoriaNome() != null && !dto.categoriaNome().isBlank()){
            Categoria categoria = new Categoria(dto.categoriaNome());
            novaDespesa.setCategoria(categoria);
        } else {
            throw new IllegalArgumentException("Informe a categoria");
        }

        return repository.save(novaDespesa);
    }

    public Optional<Despesa> obterDespesa (Long id){
        return repository.findById(id);
    }

    public List<Despesa> obterDespesas (){
        return repository.findAll();
    }
}