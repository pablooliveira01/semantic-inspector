package com.example.demo.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.ArquivoFonte;
import com.example.demo.model.Escopo;
import com.example.demo.model.Simbolo;
import com.example.demo.model.UsoSimbolo;
import com.example.demo.repository.ArquivoFonteRepository;
import com.example.demo.repository.EscopoRepository;
import com.example.demo.repository.SimboloRepository;
import com.example.demo.repository.UsoSimboloRepository;
import com.example.demo.service.ShadowingService;

@Controller
@RequestMapping("/inspecao")
public class InspecionadorController {

    private final ArquivoFonteRepository arquivoRepository;
    private final EscopoRepository       escopoRepository;
    private final SimboloRepository      simboloRepository;
    private final UsoSimboloRepository   usoRepository;
    private final ShadowingService       shadowingService;

    public InspecionadorController(ArquivoFonteRepository arquivoRepository,
                                   EscopoRepository escopoRepository,
                                   SimboloRepository simboloRepository,
                                   UsoSimboloRepository usoRepository,
                                   ShadowingService shadowingService) {
        this.arquivoRepository = arquivoRepository;
        this.escopoRepository  = escopoRepository;
        this.simboloRepository = simboloRepository;
        this.usoRepository     = usoRepository;
        this.shadowingService  = shadowingService;
    }

    // =========================================================
    // PAINEL PRINCIPAL
    // =========================================================
    @GetMapping("/{arquivoId}")
    public String painelInspecao(@PathVariable Long arquivoId, Model model) {
        ArquivoFonte arquivo = arquivoRepository.findById(arquivoId)
            .orElseThrow(() -> new IllegalArgumentException("Arquivo inválido: " + arquivoId));

        // Detecta shadowing no backend — substitui o hardcode do HTML
        Set<Long> shadowIds = shadowingService.detectarShadows(arquivo);

        model.addAttribute("arquivo",    arquivo);
        model.addAttribute("shadowIds",  shadowIds);
        model.addAttribute("novoEscopo", new Escopo());
        model.addAttribute("novoSimbolo", new Simbolo());
        model.addAttribute("novoUso",    new UsoSimbolo());

        return "painel-inspecao";
    }

    // =========================================================
    // CRUD DE ESCOPO
    // =========================================================
    @PostMapping("/escopo/salvar/{arquivoId}")
    public String salvarEscopo(@PathVariable Long arquivoId,
                               @ModelAttribute Escopo escopo) {
        ArquivoFonte arquivo = arquivoRepository.findById(arquivoId).orElseThrow();
        escopo.setArquivoFonte(arquivo);
        escopoRepository.save(escopo);
        return "redirect:/inspecao/" + arquivoId;
    }

    @GetMapping("/escopo/delete/{escopoId}")
    public String deletarEscopo(@PathVariable Long escopoId) {
        Escopo escopo = escopoRepository.findById(escopoId).orElseThrow();
        Long arquivoId = escopo.getArquivoFonte().getId();
        escopoRepository.delete(escopo);
        return "redirect:/inspecao/" + arquivoId;
    }

    // =========================================================
    // CRUD DE SÍMBOLO
    // =========================================================
    @PostMapping("/simbolo/salvar/{arquivoId}")
    public String salvarSimbolo(@PathVariable Long arquivoId,
                                @ModelAttribute Simbolo simbolo,
                                @RequestParam Long escopoId) {
        Escopo escopo = escopoRepository.findById(escopoId).orElseThrow();
        simbolo.setEscopo(escopo);
        simboloRepository.save(simbolo);
        return "redirect:/inspecao/" + arquivoId;
    }

    @GetMapping("/simbolo/delete/{simboloId}")
    public String deletarSimbolo(@PathVariable Long simboloId) {
        Simbolo simbolo = simboloRepository.findById(simboloId).orElseThrow();
        Long arquivoId = simbolo.getEscopo().getArquivoFonte().getId();
        simboloRepository.delete(simbolo);
        return "redirect:/inspecao/" + arquivoId;
    }

    // =========================================================
    // CRUD DE USO DE SÍMBOLO  ← novo
    // =========================================================

    /**
     * Salva um novo UsoSimbolo vinculado a um símbolo existente.
     * O arquivoId é necessário apenas para o redirect de volta ao painel.
     */
    @PostMapping("/uso/salvar/{arquivoId}")
    public String salvarUso(@PathVariable Long arquivoId,
                            @RequestParam Long simboloId,
                            @RequestParam Integer linhaCodigo,
                            @RequestParam String tipoOperacao) {
        Simbolo simbolo = simboloRepository.findById(simboloId).orElseThrow();

        UsoSimbolo uso = new UsoSimbolo();
        uso.setSimbolo(simbolo);
        uso.setLinhaCodigo(linhaCodigo);
        uso.setTipoOperacao(tipoOperacao);
        usoRepository.save(uso);

        return "redirect:/inspecao/" + arquivoId;
    }

    /**
     * Remove um UsoSimbolo e redireciona de volta ao painel do arquivo.
     */
    @GetMapping("/uso/delete/{usoId}")
    public String deletarUso(@PathVariable Long usoId) {
        UsoSimbolo uso = usoRepository.findById(usoId).orElseThrow();
        Long arquivoId = uso.getSimbolo().getEscopo().getArquivoFonte().getId();
        usoRepository.delete(uso);
        return "redirect:/inspecao/" + arquivoId;
    }
}
