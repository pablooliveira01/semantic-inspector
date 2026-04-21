package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Simbolo;
import com.example.demo.projection.SimboloComShadowView;
import com.example.demo.projection.SimboloResumoView;
import com.example.demo.projection.EstatisticaEscopoView;

public interface SimboloRepository extends JpaRepository<Simbolo, Long> {

    // ------------------------------------------------------------------
    // Consulta 1 — Todos os símbolos de um arquivo, ordenados por linha
    // Navega ArquivoFonte → Escopo → Simbolo via JPQL
    // ------------------------------------------------------------------
    @Query("SELECT s.lexema     AS lexema, " +
           "       s.tipoDado   AS tipoDado, " +
           "       s.linhaDeclaracao AS linhaDeclaracao, " +
           "       e.nomeEscopo AS nomeEscopo, " +
           "       e.nivelProfundidade AS nivelProfundidade " +
           "FROM Simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "ORDER BY s.linhaDeclaracao ASC")
    List<SimboloResumoView> findSimbolosByArquivoOrdenados(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 2 — Símbolos de um tipo específico em um arquivo
    // Ex.: buscar todos os 'int' em main.c
    // ------------------------------------------------------------------
    @Query("SELECT s.lexema     AS lexema, " +
           "       s.tipoDado   AS tipoDado, " +
           "       s.linhaDeclaracao AS linhaDeclaracao, " +
           "       e.nomeEscopo AS nomeEscopo, " +
           "       e.nivelProfundidade AS nivelProfundidade " +
           "FROM Simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "  AND LOWER(s.tipoDado) = LOWER(:tipo) " +
           "ORDER BY s.linhaDeclaracao ASC")
    List<SimboloResumoView> findByTipoEmArquivo(@Param("arquivoId") Long arquivoId,
                                                @Param("tipo") String tipo);

    // ------------------------------------------------------------------
    // Consulta 3 — Detecção real de SHADOWING
    // Um símbolo é shadow quando existe outro símbolo com o mesmo lexema
    // em um escopo de nível menor (mais externo) dentro do mesmo arquivo.
    // Retorna os símbolos que ocultam outro, com o nível do escopo oculto.
    // ------------------------------------------------------------------
    @Query("SELECT s.lexema          AS lexema, " +
           "       s.tipoDado        AS tipoDado, " +
           "       s.linhaDeclaracao AS linhaDeclaracao, " +
           "       e.nomeEscopo      AS nomeEscopo, " +
           "       e.nivelProfundidade AS nivelProfundidade, " +
           "       eOculto.nomeEscopo  AS nomeEscopoOculto, " +
           "       eOculto.nivelProfundidade AS nivelOculto " +
           "FROM Simbolo s " +
           "JOIN s.escopo e " +
           "JOIN Simbolo sOculto ON sOculto.lexema = s.lexema " +
           "JOIN sOculto.escopo eOculto " +
           "WHERE e.arquivoFonte.id     = :arquivoId " +
           "  AND eOculto.arquivoFonte.id = :arquivoId " +
           "  AND eOculto.nivelProfundidade < e.nivelProfundidade " +
           "ORDER BY s.linhaDeclaracao ASC")
    List<SimboloComShadowView> findShadowings(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 4 — Contagem de símbolos agrupada por escopo
    // ------------------------------------------------------------------
    @Query("SELECT e.nomeEscopo      AS nomeEscopo, " +
           "       e.nivelProfundidade AS nivelProfundidade, " +
           "       COUNT(s.id)        AS totalSimbolos " +
           "FROM Escopo e " +
           "LEFT JOIN e.simbolos s " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "GROUP BY e.id, e.nomeEscopo, e.nivelProfundidade " +
           "ORDER BY e.nivelProfundidade ASC")
    List<EstatisticaEscopoView> countSimbolosPorEscopo(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 5 — Símbolos que possuem ao menos um uso registrado
    // ------------------------------------------------------------------
    @Query("SELECT DISTINCT s.lexema AS lexema, " +
           "       s.tipoDado        AS tipoDado, " +
           "       s.linhaDeclaracao AS linhaDeclaracao, " +
           "       e.nomeEscopo      AS nomeEscopo, " +
           "       e.nivelProfundidade AS nivelProfundidade " +
           "FROM Simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "  AND SIZE(s.usos) > 0 " +
           "ORDER BY s.linhaDeclaracao ASC")
    List<SimboloResumoView> findSimbolosComUsos(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 6 — Símbolos SEM nenhum uso (variáveis declaradas mas nunca usadas)
    // Útil para detectar dead code / variáveis desnecessárias
    // ------------------------------------------------------------------
    @Query("SELECT s.lexema          AS lexema, " +
           "       s.tipoDado        AS tipoDado, " +
           "       s.linhaDeclaracao AS linhaDeclaracao, " +
           "       e.nomeEscopo      AS nomeEscopo, " +
           "       e.nivelProfundidade AS nivelProfundidade " +
           "FROM Simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId " +
           "  AND SIZE(s.usos) = 0 " +
           "ORDER BY s.linhaDeclaracao ASC")
    List<SimboloResumoView> findSimbolosSemUsos(@Param("arquivoId") Long arquivoId);

    // ------------------------------------------------------------------
    // Consulta 7 — Todos os lexemas únicos de um arquivo (sem duplicatas)
    // Usado para popular selects de filtro na UI
    // ------------------------------------------------------------------
    @Query("SELECT DISTINCT s FROM Simbolo s " +
           "JOIN s.escopo e " +
           "WHERE e.arquivoFonte.id = :arquivoId")
    List<Simbolo> findAllByArquivoId(@Param("arquivoId") Long arquivoId);
}
