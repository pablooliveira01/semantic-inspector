package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.model.ArquivoFonte;
import com.example.demo.repository.ArquivoFonteRepository;

@Controller
@RequestMapping("/arquivos")
public class ArquivoFonteController {

    private final ArquivoFonteRepository arquivoRepository;

    public ArquivoFonteController(ArquivoFonteRepository arquivoRepository) {
        this.arquivoRepository = arquivoRepository;
    }

    // Listar todos os arquivos (READ)
    @GetMapping
    public String listarArquivos(Model model) {
        model.addAttribute("arquivos", arquivoRepository.findAll());
        return "arquivos-lista"; // Nome do arquivo HTML (Etapa 3)
    }

    // Formulário para criar novo arquivo (CREATE)
    @GetMapping("/novo")
    public String novoArquivoForm(Model model) {
        model.addAttribute("arquivo", new ArquivoFonte());
        return "arquivo-form";
    }

    // Salvar o arquivo no banco
    @PostMapping
    public String salvarArquivo(@ModelAttribute ArquivoFonte arquivo) {
        arquivoRepository.save(arquivo);
        return "redirect:/arquivos";
    }

    // Formulário para editar arquivo existente (UPDATE)
    @GetMapping("/edit/{id}")
    public String editarArquivoForm(@PathVariable Long id, Model model) {
        ArquivoFonte arquivo = arquivoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de arquivo inválido: " + id));
        model.addAttribute("arquivo", arquivo);
        return "arquivo-form";
    }

    // Excluir arquivo (DELETE) - Isso apagará em cascata os escopos e símbolos!
    @GetMapping("/delete/{id}")
    public String deletarArquivo(@PathVariable Long id) {
        arquivoRepository.deleteById(id);
        return "redirect:/arquivos";
    }
}