package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ArquivoFonte;
public interface ArquivoFonteRepository extends JpaRepository<ArquivoFonte, Long> {}