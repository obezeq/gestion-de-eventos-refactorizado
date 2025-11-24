package org.example.apirest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apirest.dto.participante.ParticipanteRequestDTO;
import org.example.apirest.dto.participante.ParticipanteResponseDTO;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Participante;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.mapper.ParticipanteMapper;
import org.example.apirest.service.EventoService;
import org.example.apirest.service.ParticipanteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParticipanteController.class)
@DisplayName("ParticipanteController Tests")
class ParticipanteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ParticipanteService participanteService;

    @MockBean
    private EventoService eventoService;

    @MockBean
    private ParticipanteMapper participanteMapper;

    @Test
    @DisplayName("GET /api/v1/participantes - Should return page of participantes")
    void testGetAllParticipantes() throws Exception {
        // Given
        Participante p1 = new Participante();
        p1.setId(1L);
        p1.setNombre("Part 1");
        p1.setEmail("p1@test.com");

        ParticipanteResponseDTO dto1 = ParticipanteResponseDTO.builder()
                .id(1L)
                .nombre("Part 1")
                .email("p1@test.com")
                .build();

        List<Participante> participantes = Arrays.asList(p1);
        Page<Participante> page = new PageImpl<>(participantes, PageRequest.of(0, 10), 1);

        when(participanteService.findAll(any(PageRequest.class))).thenReturn(page);
        when(participanteMapper.toResponseDTO(p1)).thenReturn(dto1);

        // When & Then
        mockMvc.perform(get("/api/v1/participantes")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nombre").value("Part 1"));

        verify(participanteService, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/participantes/{id} - Should return participante by id")
    void testGetParticipanteById() throws Exception {
        // Given
        Long id = 1L;
        Participante participante = new Participante();
        participante.setId(id);
        participante.setNombre("Test Part");
        participante.setEmail("test@test.com");

        when(participanteService.findById(id)).thenReturn(participante);
        when(participanteMapper.toWithEventoDTO(participante)).thenReturn(any());

        // When & Then
        mockMvc.perform(get("/api/v1/participantes/{id}", id))
                .andExpect(status().isOk());

        verify(participanteService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/participantes/{id} - Should return 404 when not found")
    void testGetParticipanteByIdNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(participanteService.findById(id))
                .thenThrow(new ResourceNotFoundException("Participante", "id", id));

        // When & Then
        mockMvc.perform(get("/api/v1/participantes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Participante no encontrado con id: '999'"));

        verify(participanteService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/participantes/search - Should search by email")
    void testSearchByEmail() throws Exception {
        // Given
        String email = "test";
        Participante p1 = new Participante();
        p1.setId(1L);
        p1.setNombre("Test Part");
        p1.setEmail("test@test.com");

        ParticipanteResponseDTO dto1 = ParticipanteResponseDTO.builder()
                .id(1L)
                .nombre("Test Part")
                .email("test@test.com")
                .build();

        when(participanteService.searchByEmail(email)).thenReturn(Arrays.asList(p1));
        when(participanteMapper.toResponseDTO(p1)).thenReturn(dto1);

        // When & Then
        mockMvc.perform(get("/api/v1/participantes/search")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("test@test.com"));

        verify(participanteService, times(1)).searchByEmail(email);
    }

    @Test
    @DisplayName("PUT /api/v1/participantes/{id} - Should update participante successfully")
    void testUpdateParticipante() throws Exception {
        // Given
        Long id = 1L;
        ParticipanteRequestDTO requestDTO = ParticipanteRequestDTO.builder()
                .nombre("Updated Part")
                .email("updated@test.com")
                .telefono("987654321")
                .eventoId(1L)
                .build();

        Evento evento = new Evento();
        evento.setId(1L);

        Participante participante = new Participante();
        participante.setNombre("Updated Part");
        participante.setEmail("updated@test.com");

        Participante updatedParticipante = new Participante();
        updatedParticipante.setId(id);
        updatedParticipante.setNombre("Updated Part");
        updatedParticipante.setEmail("updated@test.com");

        ParticipanteResponseDTO responseDTO = ParticipanteResponseDTO.builder()
                .id(id)
                .nombre("Updated Part")
                .email("updated@test.com")
                .build();

        when(eventoService.findById(1L)).thenReturn(evento);
        when(participanteMapper.toEntity(any(ParticipanteRequestDTO.class), any(Evento.class))).thenReturn(participante);
        when(participanteService.update(eq(id), any(Participante.class))).thenReturn(updatedParticipante);
        when(participanteMapper.toResponseDTO(updatedParticipante)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/participantes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Updated Part"));

        verify(participanteService, times(1)).update(eq(id), any(Participante.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/participantes/{id} - Should delete participante successfully")
    void testDeleteParticipante() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(participanteService).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/participantes/{id}", id))
                .andExpect(status().isNoContent());

        verify(participanteService, times(1)).deleteById(id);
    }
}
