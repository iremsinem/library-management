package com.example.library_management.repository;

import com.example.library_management.model.Yazar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YazarRepository extends JpaRepository<Yazar, Integer> {}
