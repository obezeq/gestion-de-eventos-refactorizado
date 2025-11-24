package org.example.apirest.repository;

import org.example.apirest.entity.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
    List<Organizador> findByNombreContainingIgnoreCase(String nombre);
    Optional<Organizador> findByEmail(String email);
    boolean existsByEmail(String email);
}
