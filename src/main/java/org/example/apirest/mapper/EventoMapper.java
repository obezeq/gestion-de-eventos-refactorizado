package org.example.apirest.mapper;

import lombok.RequiredArgsConstructor;
import org.example.apirest.dto.evento.EventoRequestDTO;
import org.example.apirest.dto.evento.EventoResponseDTO;
import org.example.apirest.dto.evento.EventoSummaryDTO;
import org.example.apirest.dto.evento.EventoWithParticipantesDTO;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventoMapper {

    @Lazy
    private final OrganizadorMapper organizadorMapper;

    @Lazy
    private final ParticipanteMapper participanteMapper;

    public Evento toEntity(EventoRequestDTO dto, Organizador organizador) {
        Evento evento = new Evento();
        evento.setTitulo(dto.getTitulo());
        evento.setDescripcion(dto.getDescripcion());
        evento.setFecha(dto.getFecha());
        evento.setUbicacion(dto.getUbicacion());
        evento.setOrganizador(organizador);
        return evento;
    }

    public EventoResponseDTO toResponseDTO(Evento entity) {
        return EventoResponseDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .fecha(entity.getFecha())
                .ubicacion(entity.getUbicacion())
                .organizador(organizadorMapper.toResponseDTO(entity.getOrganizador()))
                .build();
    }

    public EventoSummaryDTO toSummaryDTO(Evento entity) {
        return EventoSummaryDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .fecha(entity.getFecha())
                .ubicacion(entity.getUbicacion())
                .build();
    }

    public EventoWithParticipantesDTO toWithParticipantesDTO(Evento entity) {
        return EventoWithParticipantesDTO.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .fecha(entity.getFecha())
                .ubicacion(entity.getUbicacion())
                .organizador(organizadorMapper.toResponseDTO(entity.getOrganizador()))
                .participantes(entity.getParticipantes() != null ?
                        entity.getParticipantes().stream()
                                .map(participanteMapper::toResponseDTO)
                                .collect(Collectors.toList()) :
                        null)
                .build();
    }

    public void updateEntityFromDTO(EventoRequestDTO dto, Evento entity, Organizador organizador) {
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());
        entity.setUbicacion(dto.getUbicacion());
        entity.setOrganizador(organizador);
    }
}
