package com.example.demo.projection;

/**
 * Projeção básica de símbolo com nome do escopo.
 * Usada pela maioria das consultas de listagem em SimboloRepository.
 */
public interface SimboloResumoView {
    String  getLexema();
    String  getTipoDado();
    Integer getLinhaDeclaracao();
    String  getNomeEscopo();
    Integer getNivelProfundidade();
}
