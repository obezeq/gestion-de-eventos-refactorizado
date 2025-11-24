package org.example.apirest.repository;

import org.example.apirest.entity.Participante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {

    List<Participante> findByEmailContainingIgnoreCase(String email);

    List<Participante> findByEventoId(Long eventoId);

    Optional<Participante> findByEmail(String email);

    boolean existsByEmail(String email);

}
