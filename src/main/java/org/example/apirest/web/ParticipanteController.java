package org.example.apirest.web;

import jakarta.validation.Valid;
import org.example.apirest.entity.Participante;
import org.example.apirest.service.ParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/participantes")
public class ParticipanteController {

    @Autowired
    private ParticipanteService participanteService;

    @GetMapping
    public ResponseEntity<List<Participante>> list() {
        List<Participante> participantes = participanteService.findAll();
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participante> findById(@PathVariable Long id) {
        Optional<Participante> participante = participanteService.findById(id);

        if (participante.isPresent()) {
            return ResponseEntity.ok(participante.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Participante>> searchByEmail(@RequestParam String email) {
        List<Participante> participantes = participanteService.findByEmail(email);
        return ResponseEntity.ok(participantes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participante> update(@PathVariable Long id, @Valid @RequestBody Participante participante) {
        Optional<Participante> updatedParticipante = participanteService.update(id, participante);

        if (updatedParticipante.isPresent()) {
            return ResponseEntity.ok(updatedParticipante.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (participanteService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
