package org.example.apirest.repository;

import org.example.apirest.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    List <Evento> findByTituloContainingIgnoreCase(String titulo);
}
