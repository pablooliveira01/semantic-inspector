package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Escopo;

public interface EscopoRepository extends JpaRepository<Escopo, Long> {

    // ------------------------------------------------------------------
    // Consulta 1 — Todos os escopos de um arquivo, ordenados por nível
    // ------------------------------------------------------------------
    List<Escopo> findByArquivoFonteIdOrderByNivelProfundidadeAsc(Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 2 — Escopos de um nível específico (ex.: todos os blocos if)
    // ------------------------------------------------------------------
    @Query("SELECT e FROM Escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "  AND e.nivelProfundidade = :nivel " +
           "ORDER BY e.id ASC")
    List<Escopo> findByArquivoEsNivel(@Param("arquivoId") Long arquivoId,
                                      @Param("nivel") Integer nivel);

    // ------------------------------------------------------------------
    // Consulta 3 — Escopos que contêm ao menos um símbolo
    // ------------------------------------------------------------------
    @Query("SELECT e FROM Escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "  AND SIZE(e.simbolos) > 0 " +
           "ORDER BY e.nivelProfundidade ASC")
    List<Escopo> findEscopossComSimbolos(@Param("arquivoId") Long arquivoId);
}
