package org.example.apirest.dto.organizador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizadorResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
}
