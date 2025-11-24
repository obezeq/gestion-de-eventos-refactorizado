package org.example.apirest.mapper;

import org.example.apirest.dto.organizador.OrganizadorRequestDTO;
import org.example.apirest.dto.organizador.OrganizadorResponseDTO;
import org.example.apirest.dto.organizador.OrganizadorWithEventosDTO;
import org.example.apirest.entity.Organizador;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrganizadorMapper {

    private final EventoMapper eventoMapper;

    public OrganizadorMapper(@Lazy EventoMapper eventoMapper) {
        this.eventoMapper = eventoMapper;
    }

    public Organizador toEntity(OrganizadorRequestDTO dto) {
        Organizador organizador = new Organizador();
        organizador.setNombre(dto.getNombre());
        organizador.setEmail(dto.getEmail());
        organizador.setTelefono(dto.getTelefono());
        return organizador;
    }

    public OrganizadorResponseDTO toResponseDTO(Organizador entity) {
        return OrganizadorResponseDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .build();
    }

    public OrganizadorWithEventosDTO toWithEventosDTO(Organizador entity) {
        return OrganizadorWithEventosDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .eventos(entity.getEventos() != null ?
                        entity.getEventos().stream()
                                .map(eventoMapper::toSummaryDTO)
                                .collect(Collectors.toList()) :
                        null)
                .build();
    }

    public void updateEntityFromDTO(OrganizadorRequestDTO dto, Organizador entity) {
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
    }
}
