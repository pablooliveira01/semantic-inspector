package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Escopo;
public interface EscopoRepository extends JpaRepository<Escopo, Long> {}