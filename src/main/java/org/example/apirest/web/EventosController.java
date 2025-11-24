package org.example.apirest.web;

import jakarta.validation.Valid;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Participante;
import org.example.apirest.service.EventoService;
import org.example.apirest.service.ParticipanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/eventos")
public class EventosController {

    @Autowired
    EventoService eventoService;

    @Autowired
    ParticipanteService participanteService;

    @GetMapping
    public ResponseEntity<List<Evento>> list() {
        List<Evento> eventos = eventoService.findAll();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> findById(@PathVariable Long id) {
        Optional<Evento> evento = eventoService.findById(id);
        return evento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Evento> save(@Valid @RequestBody Evento evento) {
        Evento savedEvento = eventoService.save(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> update(@PathVariable Long id, @Valid @RequestBody Evento evento) {
        Optional<Evento> savedEvento = eventoService.update(id, evento);
        return savedEvento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Evento> delete(@PathVariable Long id) {
        if (eventoService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Evento>> search(@RequestParam String titulo) {
        List<Evento> eventos = eventoService.findByTitulo(titulo);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/echo")
    public String echo() { return "echo"; }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<?> addParticipante(@PathVariable Long id, @Valid @RequestBody Participante participante) {
        if (participanteService.emailExists(participante.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya esta registrado");
        }

        Optional<Participante> created = participanteService.save(id, participante);

        if (created.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(created.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento no encontrado");
        }
    }

    @GetMapping("/{id}/participantes")
    public ResponseEntity<List<Participante>> getEventoParticipantes(@PathVariable Long id) {
        List<Participante> participantes = participanteService.findByEventoId(id);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}/participantes/{participanteId}")
    public ResponseEntity<Participante> getEventoParticipante(@PathVariable Long id, @PathVariable Long participanteId) {
        Optional<Participante> participante = participanteService.findById(participanteId);

        if (participante.isPresent() && participante.get().getEvento() != null
                && participante.get().getEvento().getId().equals(id)) {
            return ResponseEntity.ok(participante.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
