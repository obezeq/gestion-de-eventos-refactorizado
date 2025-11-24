package org.example.apirest.dto.evento;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.apirest.dto.organizador.OrganizadorResponseDTO;
import org.example.apirest.dto.participante.ParticipanteResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoWithParticipantesDTO {
    private Long id;
    private String titulo;
    private String descripcion;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fecha;

    private String ubicacion;
    private OrganizadorResponseDTO organizador;
    private List<ParticipanteResponseDTO> participantes;
}
