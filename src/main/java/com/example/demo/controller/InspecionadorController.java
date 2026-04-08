package com.example.demo.controller;

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
import com.example.demo.repository.ArquivoFonteRepository;
import com.example.demo.repository.EscopoRepository;
import com.example.demo.repository.SimboloRepository;

@Controller
@RequestMapping("/inspecao")
public class InspecionadorController {

    private final ArquivoFonteRepository arquivoRepository;
    private final EscopoRepository escopoRepository;
    private final SimboloRepository simboloRepository;

    public InspecionadorController(ArquivoFonteRepository arquivoRepository, 
                                   EscopoRepository escopoRepository, 
                                   SimboloRepository simboloRepository) {
        this.arquivoRepository = arquivoRepository;
        this.escopoRepository = escopoRepository;
        this.simboloRepository = simboloRepository;
    }

    // ==========================================
    // PAINEL DE INSPEÇÃO DO ARQUIVO (A Tabela de Símbolos visual)
    // ==========================================
    @GetMapping("/{arquivoId}")
    public String painelInspecao(@PathVariable Long arquivoId, Model model) {
        ArquivoFonte arquivo = arquivoRepository.findById(arquivoId)
            .orElseThrow(() -> new IllegalArgumentException("Arquivo inválido: " + arquivoId));
        
        model.addAttribute("arquivo", arquivo);
        
        // Passamos objetos vazios para os modais/formulários de criação na mesma tela
        model.addAttribute("novoEscopo", new Escopo());
        model.addAttribute("novoSimbolo", new Simbolo());
        
        return "painel-inspecao"; // Esta será a nossa tela mais legal na Etapa 3
    }

    // ==========================================
    // CRUD DE ESCOPO (Direto pelo painel)
    // ==========================================
    @PostMapping("/escopo/salvar/{arquivoId}")
    public String salvarEscopo(@PathVariable Long arquivoId, @ModelAttribute Escopo escopo) {
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

    // ==========================================
    // CRUD DE SÍMBOLO (Variáveis)
    // ==========================================
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
}