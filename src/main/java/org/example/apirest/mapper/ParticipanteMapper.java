package org.example.apirest.mapper;

import lombok.RequiredArgsConstructor;
import org.example.apirest.dto.participante.ParticipanteRequestDTO;
import org.example.apirest.dto.participante.ParticipanteResponseDTO;
import org.example.apirest.dto.participante.ParticipanteWithEventoDTO;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Participante;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipanteMapper {

    @Lazy
    private final EventoMapper eventoMapper;

    public Participante toEntity(ParticipanteRequestDTO dto, Evento evento) {
        Participante participante = new Participante();
        participante.setNombre(dto.getNombre());
        participante.setEmail(dto.getEmail());
        participante.setTelefono(dto.getTelefono());
        participante.setEvento(evento);
        return participante;
    }

    public ParticipanteResponseDTO toResponseDTO(Participante entity) {
        return ParticipanteResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .build();
    }

    public ParticipanteWithEventoDTO toWithEventoDTO(Participante entity) {
        return ParticipanteWithEventoDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .evento(eventoMapper.toSummaryDTO(entity.getEvento()))
                .build();
    }

    public void updateEntityFromDTO(ParticipanteRequestDTO dto, Participante entity, Evento evento) {
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setEvento(evento);
    }
}
