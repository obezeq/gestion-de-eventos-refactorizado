package org.example.apirest.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.apirest.dto.evento.EventoRequestDTO;
import org.example.apirest.dto.evento.EventoResponseDTO;
import org.example.apirest.dto.evento.EventoWithParticipantesDTO;
import org.example.apirest.dto.participante.ParticipanteRequestDTO;
import org.example.apirest.dto.participante.ParticipanteResponseDTO;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.example.apirest.entity.Participante;
import org.example.apirest.mapper.EventoMapper;
import org.example.apirest.mapper.ParticipanteMapper;
import org.example.apirest.service.EventoService;
import org.example.apirest.service.OrganizadorService;
import org.example.apirest.service.ParticipanteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/eventos")
@RequiredArgsConstructor
public class EventosController {

    private final EventoService eventoService;
    private final OrganizadorService organizadorService;
    private final ParticipanteService participanteService;
    private final EventoMapper eventoMapper;
    private final ParticipanteMapper participanteMapper;

    @GetMapping
    public ResponseEntity<Page<EventoResponseDTO>> getAllEventos(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<EventoResponseDTO> eventos = eventoService.findAll(pageable)
                .map(eventoMapper::toResponseDTO);
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoWithParticipantesDTO> getEventoById(@PathVariable Long id) {
        Evento evento = eventoService.findById(id);
        return ResponseEntity.ok(eventoMapper.toWithParticipantesDTO(evento));
    }

    @PostMapping
    public ResponseEntity<EventoResponseDTO> createEvento(
            @Valid @RequestBody EventoRequestDTO requestDTO) {
        Organizador organizador = organizadorService.findById(requestDTO.getOrganizadorId());
        Evento evento = eventoMapper.toEntity(requestDTO, organizador);
        Evento savedEvento = eventoService.save(evento);
        return new ResponseEntity<>(eventoMapper.toResponseDTO(savedEvento), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> updateEvento(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequestDTO requestDTO) {
        Organizador organizador = organizadorService.findById(requestDTO.getOrganizadorId());
        Evento evento = eventoMapper.toEntity(requestDTO, organizador);
        Evento updatedEvento = eventoService.update(id, evento);
        return ResponseEntity.ok(eventoMapper.toResponseDTO(updatedEvento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        eventoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventoResponseDTO>> searchEventosByTitulo(
            @RequestParam String titulo) {
        List<EventoResponseDTO> eventos = eventoService.searchByTitulo(titulo)
                .stream()
                .map(eventoMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventos);
    }

    @PostMapping("/{id}/participantes")
    public ResponseEntity<ParticipanteResponseDTO> addParticipanteToEvento(
            @PathVariable Long id,
            @Valid @RequestBody ParticipanteRequestDTO requestDTO) {
        Evento evento = eventoService.findById(id);
        Participante participante = participanteMapper.toEntity(requestDTO, evento);
        Participante savedParticipante = participanteService.save(participante);
        return new ResponseEntity<>(participanteMapper.toResponseDTO(savedParticipante), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/participantes")
    public ResponseEntity<Page<ParticipanteResponseDTO>> getParticipantesByEvento(
            @PathVariable Long id,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ParticipanteResponseDTO> participantes = participanteService.findByEventoId(id, pageable)
                .map(participanteMapper::toResponseDTO);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}/participantes/{participanteId}")
    public ResponseEntity<ParticipanteResponseDTO> getParticipanteByEvento(
            @PathVariable Long id,
            @PathVariable Long participanteId) {
        Participante participante = participanteService.findById(participanteId);
        return ResponseEntity.ok(participanteMapper.toResponseDTO(participante));
    }

    @GetMapping("/echo")
    public String echo() {
        return "echo";
    }
}
