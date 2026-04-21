package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.ArquivoFonte;
import com.example.demo.projection.ArquivoEstatisticaView;

public interface ArquivoFonteRepository extends JpaRepository<ArquivoFonte, Long> {

    // ------------------------------------------------------------------
    // Consulta 1 — Arquivos ordenados por data de análise (mais recentes primeiro)
    // ------------------------------------------------------------------
    List<ArquivoFonte> findAllByOrderByDataAnaliseDesc();

    // ------------------------------------------------------------------
    // Consulta 2 — Busca por nome (case-insensitive, parcial)
    // Ex.: buscar "main" encontra "main.c" e "main.py"
    // ------------------------------------------------------------------
    @Query("SELECT a FROM ArquivoFonte a " +
           "WHERE LOWER(a.nomeArquivo) LIKE LOWER(CONCAT('%', :termo, '%')) " +
           "ORDER BY a.dataAnalise DESC")
    List<ArquivoFonte> findByNomeContendo(@Param("termo") String termo);

    // ------------------------------------------------------------------
    // Consulta 3 — Estatísticas por arquivo: total de escopos, símbolos e usos
    // Usa COUNT com LEFT JOIN para incluir arquivos sem dados ainda
    // ------------------------------------------------------------------
    @Query("SELECT a.id            AS id, " +
           "       a.nomeArquivo   AS nomeArquivo, " +
           "       a.dataAnalise   AS dataAnalise, " +
           "       COUNT(DISTINCT e.id) AS totalEscopos, " +
           "       COUNT(DISTINCT s.id) AS totalSimbolos, " +
           "       COUNT(DISTINCT u.id) AS totalUsos " +
           "FROM ArquivoFonte a " +
           "LEFT JOIN a.escopos e " +
           "LEFT JOIN e.simbolos s " +
           "LEFT JOIN s.usos u " +
           "GROUP BY a.id, a.nomeArquivo, a.dataAnalise " +
           "ORDER BY a.dataAnalise DESC")
    List<ArquivoEstatisticaView> findEstatisticasGerais();

    // ------------------------------------------------------------------
    // Consulta 4 — Arquivos que possuem shadowing (têm símbolo repetido
    // em escopos de níveis diferentes)
    // ------------------------------------------------------------------
    @Query("SELECT DISTINCT a FROM ArquivoFonte a " +
           "JOIN a.escopos e1 " +
           "JOIN e1.simbolos s1 " +
           "JOIN a.escopos e2 " +
           "JOIN e2.simbolos s2 " +
           "WHERE s1.lexema = s2.lexema " +
           "  AND e1.nivelProfundidade <> e2.nivelProfundidade " +
           "ORDER BY a.dataAnalise DESC")
    List<ArquivoFonte> findArquivosComShadowing();
}
