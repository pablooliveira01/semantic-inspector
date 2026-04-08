package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "arquivos_fonte")
public class ArquivoFonte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeArquivo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String codigoConteudo;

    @Column(nullable = false)
    private LocalDateTime dataAnalise;

    // Relacionamento 1:N com Escopo
    @OneToMany(mappedBy = "arquivoFonte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Escopo> escopos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.dataAnalise = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getCodigoConteudo() { return codigoConteudo; }
    public void setCodigoConteudo(String codigoConteudo) { this.codigoConteudo = codigoConteudo; }
    public LocalDateTime getDataAnalise() { return dataAnalise; }
    public void setDataAnalise(LocalDateTime dataAnalise) { this.dataAnalise = dataAnalise; }
    public List<Escopo> getEscopos() { return escopos; }
    public void setEscopos(List<Escopo> escopos) { this.escopos = escopos; }
}