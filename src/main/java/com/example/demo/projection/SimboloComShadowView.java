package com.example.demo.projection;

/**
 * Projeção estendida para a consulta de shadowing.
 * Inclui os dados do escopo que está sendo ocultado (o escopo externo
 * cujo símbolo de mesmo nome é sombreado pelo símbolo atual).
 */
public interface SimboloComShadowView {
    String  getLexema();
    String  getTipoDado();
    Integer getLinhaDeclaracao();
    String  getNomeEscopo();           // escopo onde o shadow ocorre (interno)
    Integer getNivelProfundidade();    // nível do escopo interno

    String  getNomeEscopoOculto();     // escopo externo que está sendo ocultado
    Integer getNivelOculto();          // nível do escopo externo
}
