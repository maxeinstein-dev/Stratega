package br.com.maxsueleinstein.stratega.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "despesas")
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String descricao;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Categoria categoria;
    private Double valorPrevisto;
    private Double valorGasto;

    public Despesa(){}

    public Despesa(String nome, Categoria categoria, Double valorPrevisto) {
        this.nome = nome;
        this.categoria = categoria;
        this.valorPrevisto = valorPrevisto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Double getValorPrevisto() {
        return valorPrevisto;
    }

    public void setValorPrevisto(Double valorPrevisto) {
        this.valorPrevisto = valorPrevisto;
    }

    public Double getValorGasto() {
        return valorGasto;
    }

    public void setValorGasto(Double valorGasto) {
        this.valorGasto = valorGasto;
    }
}