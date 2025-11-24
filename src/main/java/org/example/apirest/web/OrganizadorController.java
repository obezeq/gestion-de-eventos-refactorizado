package org.example.apirest.web;

import jakarta.validation.Valid;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.example.apirest.service.OrganizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/organizadores")
public class OrganizadorController {

    @Autowired
    private OrganizadorService organizadorService;

    @GetMapping
    public ResponseEntity<List<Organizador>> list() {
        List<Organizador> organizadores = organizadorService.findAll();
        return ResponseEntity.ok(organizadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizador> findById(@PathVariable Long id) {
        Optional<Organizador> organizador = organizadorService.findById(id);
        if (organizador.isPresent()) {
            return ResponseEntity.ok(organizador.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Organizador organizador) {
        if (organizadorService.emailExists(organizador.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El email ya esta registrado");
        }
        Organizador savedOrganizador = organizadorService.save(organizador);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrganizador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organizador> update(@PathVariable Long id, @Valid @RequestBody Organizador organizador) {
        Optional<Organizador> updatedOrganizador = organizadorService.update(id, organizador);
        if (updatedOrganizador.isPresent()) {
            return ResponseEntity.ok(updatedOrganizador.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (organizadorService.delete(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Organizador>> search(@RequestParam String nombre) {
        List<Organizador> organizadores = organizadorService.findByNombre(nombre);
        return ResponseEntity.ok(organizadores);
    }

    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<Evento>> getOrganizadorEventos(@PathVariable Long id) {
        Optional<Organizador> organizador = organizadorService.findById(id);
        if (organizador.isPresent()) {
            return ResponseEntity.ok(organizador.get().getEventos());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
