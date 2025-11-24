package org.example.apirest.dto.organizador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.apirest.dto.evento.EventoSummaryDTO;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizadorWithEventosDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private List<EventoSummaryDTO> eventos;
}
