package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.UsoSimbolo;
import com.example.demo.projection.UsoResumoView;

public interface UsoSimboloRepository extends JpaRepository<UsoSimbolo, Long> {

    // ------------------------------------------------------------------
    // Consulta 1 — Todos os usos de um símbolo específico
    // ------------------------------------------------------------------
    List<UsoSimbolo> findBySimboloIdOrderByLinhaCodigoAsc(Long simboloId);

    // ------------------------------------------------------------------
    // Consulta 2 — Todos os usos de todos os símbolos de um arquivo,
    // ordenados por linha — usado pela animação do painel
    // ------------------------------------------------------------------
    @Query("SELECT u.linhaCodigo  AS linhaCodigo, " +
           "       u.tipoOperacao AS tipoOperacao, " +
           "       s.lexema       AS lexema, " +
           "       s.tipoDado     AS tipoDado, " +
           "       e.nomeEscopo   AS nomeEscopo " +
           "FROM UsoSimbolo u " +
           "JOIN u.simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "ORDER BY u.linhaCodigo ASC")
    List<UsoResumoView> findUsosByArquivo(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 3 — Contagem de leituras vs escritas de um arquivo
    // ------------------------------------------------------------------
    @Query("SELECT u.tipoOperacao AS tipoOperacao, COUNT(u.id) AS total " +
           "FROM UsoSimbolo u " +
           "JOIN u.simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "GROUP BY u.tipoOperacao")
    List<UsoResumoView> countUsosPorTipo(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 4 — Usos de escrita (úteis para detectar re-atribuições)
    // ------------------------------------------------------------------
    @Query("SELECT u.linhaCodigo  AS linhaCodigo, " +
           "       u.tipoOperacao AS tipoOperacao, " +
           "       s.lexema       AS lexema, " +
           "       s.tipoDado     AS tipoDado, " +
           "       e.nomeEscopo   AS nomeEscopo " +
           "FROM UsoSimbolo u " +
           "JOIN u.simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "  AND u.tipoOperacao = 'Escrita' " +
           "ORDER BY u.linhaCodigo ASC")
    List<UsoResumoView> findEscritasByArquivo(@Param("arquivoId") Long arquivoId);
}
