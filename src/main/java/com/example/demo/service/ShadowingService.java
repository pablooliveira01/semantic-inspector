package com.example.demo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.model.ArquivoFonte;
import com.example.demo.model.Escopo;
import com.example.demo.model.Simbolo;

/**
 * Serviço responsável por detectar shadowing de variáveis em um ArquivoFonte.
 *
 * Regra: um símbolo S declarado no escopo E (nível N) é um "shadow" se
 * existir outro símbolo com o mesmo lexema em qualquer escopo de nível
 * menor (mais externo) dentro do mesmo arquivo.
 *
 * Exemplo:
 *   Escopo Global (nível 0):  int x = 10;
 *   Escopo Função  (nível 1): int x = 99;  ← shadowing: oculta o x global
 */
@Service
public class ShadowingService {

    /**
     * Retorna o conjunto de IDs de símbolos que são shadows dentro do arquivo.
     * Esse Set é passado ao Model e consumido pelo template Thymeleaf com
     * th:classappend="${shadowIds.contains(simb.id) ? ' shadow-warn' : ''}".
     */
    public Set<Long> detectarShadows(ArquivoFonte arquivo) {
        Set<Long> shadowIds = new HashSet<>();

        // Para cada símbolo de nível N, verifica se existe um símbolo
        // com mesmo lexema em algum escopo de nível < N no mesmo arquivo.
        for (Escopo escopoAtual : arquivo.getEscopos()) {
            for (Simbolo simboloAtual : escopoAtual.getSimbolos()) {

                boolean isShadow = arquivo.getEscopos().stream()
                    // apenas escopos mais externos
                    .filter(e -> e.getNivelProfundidade() < escopoAtual.getNivelProfundidade())
                    // que tenham algum símbolo com o mesmo lexema
                    .anyMatch(eExterno -> eExterno.getSimbolos().stream()
                        .anyMatch(sExterno ->
                            sExterno.getLexema().equals(simboloAtual.getLexema())));

                if (isShadow) {
                    shadowIds.add(simboloAtual.getId());
                }
            }
        }

        return shadowIds;
    }
}
