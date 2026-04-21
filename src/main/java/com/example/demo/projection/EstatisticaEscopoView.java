package com.example.demo.projection;

/**
 * Projeção para a consulta de contagem de símbolos por escopo.
 */
public interface EstatisticaEscopoView {
    String  getNomeEscopo();
    Integer getNivelProfundidade();
    Long    getTotalSimbolos();
}
