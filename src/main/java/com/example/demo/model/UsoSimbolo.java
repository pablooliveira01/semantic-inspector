package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usos_simbolo")
public class UsoSimbolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer linhaCodigo;

    @Column(nullable = false, length = 50)
    private String tipoOperacao; // 'Leitura' ou 'Escrita'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "simbolo_id", nullable = false)
    private Simbolo simbolo;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getLinhaCodigo() { return linhaCodigo; }
    public void setLinhaCodigo(Integer linhaCodigo) { this.linhaCodigo = linhaCodigo; }
    public String getTipoOperacao() { return tipoOperacao; }
    public void setTipoOperacao(String tipoOperacao) { this.tipoOperacao = tipoOperacao; }
    public Simbolo getSimbolo() { return simbolo; }
    public void setSimbolo(Simbolo simbolo) { this.simbolo = simbolo; }
}