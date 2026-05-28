package com.example.library_management.repository;

import com.example.library_management.model.Yayinevi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YayineviRepository extends JpaRepository<Yayinevi, Integer> {}
