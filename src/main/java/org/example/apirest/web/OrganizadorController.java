package org.example.apirest.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.apirest.dto.evento.EventoSummaryDTO;
import org.example.apirest.dto.organizador.OrganizadorRequestDTO;
import org.example.apirest.dto.organizador.OrganizadorResponseDTO;
import org.example.apirest.entity.Organizador;
import org.example.apirest.mapper.EventoMapper;
import org.example.apirest.mapper.OrganizadorMapper;
import org.example.apirest.service.OrganizadorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/organizadores")
@RequiredArgsConstructor
public class OrganizadorController {

    private final OrganizadorService organizadorService;
    private final OrganizadorMapper organizadorMapper;
    private final EventoMapper eventoMapper;

    @GetMapping
    public ResponseEntity<Page<OrganizadorResponseDTO>> getAllOrganizadores(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<OrganizadorResponseDTO> organizadores = organizadorService.findAll(pageable)
                .map(organizadorMapper::toResponseDTO);
        return ResponseEntity.ok(organizadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizadorResponseDTO> getOrganizadorById(@PathVariable Long id) {
        Organizador organizador = organizadorService.findById(id);
        return ResponseEntity.ok(organizadorMapper.toResponseDTO(organizador));
    }

    @PostMapping
    public ResponseEntity<OrganizadorResponseDTO> createOrganizador(
            @Valid @RequestBody OrganizadorRequestDTO requestDTO) {
        Organizador organizador = organizadorMapper.toEntity(requestDTO);
        Organizador savedOrganizador = organizadorService.save(organizador);
        return new ResponseEntity<>(organizadorMapper.toResponseDTO(savedOrganizador), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizadorResponseDTO> updateOrganizador(
            @PathVariable Long id,
            @Valid @RequestBody OrganizadorRequestDTO requestDTO) {
        Organizador organizador = organizadorMapper.toEntity(requestDTO);
        Organizador updatedOrganizador = organizadorService.update(id, organizador);
        return ResponseEntity.ok(organizadorMapper.toResponseDTO(updatedOrganizador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizador(@PathVariable Long id) {
        organizadorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrganizadorResponseDTO>> searchOrganizadoresByNombre(
            @RequestParam String nombre) {
        List<OrganizadorResponseDTO> organizadores = organizadorService.searchByNombre(nombre)
                .stream()
                .map(organizadorMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(organizadores);
    }

    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<EventoSummaryDTO>> getEventosByOrganizador(@PathVariable Long id) {
        Organizador organizador = organizadorService.findById(id);
        List<EventoSummaryDTO> eventos = organizador.getEventos()
                .stream()
                .map(eventoMapper::toSummaryDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventos);
    }
}
