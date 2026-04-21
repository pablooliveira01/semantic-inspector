package com.example.demo.projection;

/**
 * Projeção para consultas sobre UsoSimbolo.
 * Alguns campos são opcionais dependendo da consulta que os popula
 * (ex.: countUsosPorTipo não retorna linhaCodigo nem lexema).
 */
public interface UsoResumoView {
    Integer getLinhaCodigo();
    String  getTipoOperacao();
    String  getLexema();
    String  getTipoDado();
    String  getNomeEscopo();
    // Campo extra retornado por countUsosPorTipo
    Long    getTotal();
}
