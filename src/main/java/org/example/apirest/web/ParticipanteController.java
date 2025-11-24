package org.example.apirest.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.apirest.dto.participante.ParticipanteRequestDTO;
import org.example.apirest.dto.participante.ParticipanteResponseDTO;
import org.example.apirest.dto.participante.ParticipanteWithEventoDTO;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Participante;
import org.example.apirest.mapper.ParticipanteMapper;
import org.example.apirest.service.EventoService;
import org.example.apirest.service.ParticipanteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/participantes")
@RequiredArgsConstructor
public class ParticipanteController {

    private final ParticipanteService participanteService;
    private final EventoService eventoService;
    private final ParticipanteMapper participanteMapper;

    @GetMapping
    public ResponseEntity<Page<ParticipanteResponseDTO>> getAllParticipantes(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<ParticipanteResponseDTO> participantes = participanteService.findAll(pageable)
                .map(participanteMapper::toResponseDTO);
        return ResponseEntity.ok(participantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParticipanteWithEventoDTO> getParticipanteById(@PathVariable Long id) {
        Participante participante = participanteService.findById(id);
        return ResponseEntity.ok(participanteMapper.toWithEventoDTO(participante));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ParticipanteResponseDTO>> searchParticipantesByEmail(
            @RequestParam String email) {
        List<ParticipanteResponseDTO> participantes = participanteService.searchByEmail(email)
                .stream()
                .map(participanteMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(participantes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParticipanteResponseDTO> updateParticipante(
            @PathVariable Long id,
            @Valid @RequestBody ParticipanteRequestDTO requestDTO) {
        Evento evento = eventoService.findById(requestDTO.getEventoId());
        Participante participante = participanteMapper.toEntity(requestDTO, evento);
        Participante updatedParticipante = participanteService.update(id, participante);
        return ResponseEntity.ok(participanteMapper.toResponseDTO(updatedParticipante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipante(@PathVariable Long id) {
        participanteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
