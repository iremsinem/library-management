package com.example.library_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.library_management.model.OduncIslemi;

@Repository
public interface OduncIslemRepository extends JpaRepository<OduncIslemi, Integer> {
    // JpaRepository sayesinde findAll(), save(), deleteById() gibi metodlar otomatik gelir.
}