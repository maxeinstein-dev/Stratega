package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.dto.ReceitaRequestDTO;
import br.com.maxsueleinstein.stratega.domain.model.Categoria;
import br.com.maxsueleinstein.stratega.domain.model.Receita;
import br.com.maxsueleinstein.stratega.repository.CategoriaRepository;
import br.com.maxsueleinstein.stratega.repository.ReceitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReceitaService {

    private final ReceitaRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ReceitaService(ReceitaRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public Receita criarReceita (ReceitaRequestDTO dto){
        Receita novaReceita = new Receita();
        novaReceita.setNome(dto.nome());
        novaReceita.setValorPrevisto(dto.valorPrevisto());

        if(dto.categoriaId() != null){
            novaReceita.setCategoria(categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada")));
        } else if (dto.categoriaNome() != null && !dto.categoriaNome().isBlank()){
            Categoria categoria = new Categoria(dto.categoriaNome());
            novaReceita.setCategoria(categoria);
        } else {
            throw new IllegalArgumentException("Informe a categoria");
        }

        return repository.save(novaReceita);
    }

    public Optional<Receita> obterReceita (Long id){
        return repository.findById(id);
    }

    public List<Receita> obterReceitas (){
        return repository.findAll();
    }
}