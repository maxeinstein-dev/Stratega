package br.com.maxsueleinstein.stratega.service;

import br.com.maxsueleinstein.stratega.domain.model.Categoria;
import br.com.maxsueleinstein.stratega.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public Categoria criarCategoria (Categoria categoria){
        return repository.save(categoria);
    }

    public List<Categoria> obterCategoriaPorNome (String nome){
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Categoria> obterCategorias (){
        return repository.findAll();
    }

    public Optional<Categoria> obterCategoria(Long id) {
        return repository.findById(id);
    }
}