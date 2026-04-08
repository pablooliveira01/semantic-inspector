package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "escopos")
public class Escopo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeEscopo;

    @Column(nullable = false)
    private Integer nivelProfundidade; // 0 = Global, 1 = Função, 2 = Bloco If, etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arquivo_id", nullable = false)
    private ArquivoFonte arquivoFonte;

    @OneToMany(mappedBy = "escopo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Simbolo> simbolos = new ArrayList<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeEscopo() { return nomeEscopo; }
    public void setNomeEscopo(String nomeEscopo) { this.nomeEscopo = nomeEscopo; }
    public Integer getNivelProfundidade() { return nivelProfundidade; }
    public void setNivelProfundidade(Integer nivelProfundidade) { this.nivelProfundidade = nivelProfundidade; }
    public ArquivoFonte getArquivoFonte() { return arquivoFonte; }
    public void setArquivoFonte(ArquivoFonte arquivoFonte) { this.arquivoFonte = arquivoFonte; }
    public List<Simbolo> getSimbolos() { return simbolos; }
    public void setSimbolos(List<Simbolo> simbolos) { this.simbolos = simbolos; }
}