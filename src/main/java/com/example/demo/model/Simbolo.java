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
@Table(name = "simbolos")
public class Simbolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String lexema; // Nome da variável (ex: 'contador')

    @Column(nullable = false, length = 50)
    private String tipoDado; // (ex: 'int', 'float')

    @Column(nullable = false)
    private Integer linhaDeclaracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escopo_id", nullable = false)
    private Escopo escopo;

    @OneToMany(mappedBy = "simbolo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsoSimbolo> usos = new ArrayList<>();

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLexema() { return lexema; }
    public void setLexema(String lexema) { this.lexema = lexema; }
    public String getTipoDado() { return tipoDado; }
    public void setTipoDado(String tipoDado) { this.tipoDado = tipoDado; }
    public Integer getLinhaDeclaracao() { return linhaDeclaracao; }
    public void setLinhaDeclaracao(Integer linhaDeclaracao) { this.linhaDeclaracao = linhaDeclaracao; }
    public Escopo getEscopo() { return escopo; }
    public void setEscopo(Escopo escopo) { this.escopo = escopo; }
    public List<UsoSimbolo> getUsos() { return usos; }
    public void setUsos(List<UsoSimbolo> usos) { this.usos = usos; }
}