package com.example.demo.projection;

import java.time.LocalDateTime;

/**
 * Projeção para a consulta de estatísticas gerais por arquivo.
 * Retornada por ArquivoFonteRepository.findEstatisticasGerais().
 */
public interface ArquivoEstatisticaView {
    Long          getId();
    String        getNomeArquivo();
    LocalDateTime getDataAnalise();
    Long          getTotalEscopos();
    Long          getTotalSimbolos();
    Long          getTotalUsos();
}
