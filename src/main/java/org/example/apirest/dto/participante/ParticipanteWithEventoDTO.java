package org.example.apirest.dto.participante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.apirest.dto.evento.EventoSummaryDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteWithEventoDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private EventoSummaryDTO evento;
}
