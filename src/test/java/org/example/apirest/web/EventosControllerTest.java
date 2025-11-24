package org.example.apirest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apirest.dto.evento.EventoRequestDTO;
import org.example.apirest.dto.evento.EventoResponseDTO;
import org.example.apirest.dto.organizador.OrganizadorResponseDTO;
import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.mapper.EventoMapper;
import org.example.apirest.mapper.ParticipanteMapper;
import org.example.apirest.service.EventoService;
import org.example.apirest.service.OrganizadorService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventosController.class)
@DisplayName("EventosController Tests")
class EventosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventoService eventoService;

    @MockBean
    private OrganizadorService organizadorService;

    @MockBean
    private ParticipanteService participanteService;

    @MockBean
    private EventoMapper eventoMapper;

    @MockBean
    private ParticipanteMapper participanteMapper;

    @Test
    @DisplayName("GET /api/v1/eventos - Should return page of eventos")
    void testGetAllEventos() throws Exception {
        // Given
        Organizador org = new Organizador();
        org.setId(1L);
        org.setNombre("Test Org");

        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setTitulo("Evento 1");
        evento1.setDescripcion("Descripcion 1");
        evento1.setOrganizador(org);

        OrganizadorResponseDTO orgDTO = OrganizadorResponseDTO.builder()
                .id(1L)
                .nombre("Test Org")
                .build();

        EventoResponseDTO dto1 = EventoResponseDTO.builder()
                .id(1L)
                .titulo("Evento 1")
                .descripcion("Descripcion 1")
                .organizador(orgDTO)
                .build();

        List<Evento> eventos = Arrays.asList(evento1);
        Page<Evento> page = new PageImpl<>(eventos, PageRequest.of(0, 10), 1);

        when(eventoService.findAll(any(PageRequest.class))).thenReturn(page);
        when(eventoMapper.toResponseDTO(evento1)).thenReturn(dto1);

        // When & Then
        mockMvc.perform(get("/api/v1/eventos")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].titulo").value("Evento 1"));

        verify(eventoService, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/eventos/{id} - Should return evento by id")
    void testGetEventoById() throws Exception {
        // Given
        Long id = 1L;
        Organizador org = new Organizador();
        org.setId(1L);

        Evento evento = new Evento();
        evento.setId(id);
        evento.setTitulo("Test Evento");
        evento.setDescripcion("Descripcion");
        evento.setOrganizador(org);

        when(eventoService.findById(id)).thenReturn(evento);
        when(eventoMapper.toWithParticipantesDTO(evento)).thenReturn(any());

        // When & Then
        mockMvc.perform(get("/api/v1/eventos/{id}", id))
                .andExpect(status().isOk());

        verify(eventoService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/eventos/{id} - Should return 404 when not found")
    void testGetEventoByIdNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(eventoService.findById(id))
                .thenThrow(new ResourceNotFoundException("Evento", "id", id));

        // When & Then
        mockMvc.perform(get("/api/v1/eventos/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Evento no encontrado con id: '999'"));

        verify(eventoService, times(1)).findById(id);
    }

    @Test
    @DisplayName("POST /api/v1/eventos - Should create evento successfully")
    void testCreateEvento() throws Exception {
        // Given
        EventoRequestDTO requestDTO = EventoRequestDTO.builder()
                .titulo("New Evento")
                .descripcion("New Description")
                .fecha(LocalDateTime.now().plusDays(30))
                .ubicacion("Madrid")
                .organizadorId(1L)
                .build();

        Organizador organizador = new Organizador();
        organizador.setId(1L);

        Evento evento = new Evento();
        evento.setTitulo("New Evento");
        evento.setDescripcion("New Description");
        evento.setOrganizador(organizador);

        Evento savedEvento = new Evento();
        savedEvento.setId(1L);
        savedEvento.setTitulo("New Evento");
        savedEvento.setDescripcion("New Description");
        savedEvento.setOrganizador(organizador);

        OrganizadorResponseDTO orgDTO = OrganizadorResponseDTO.builder()
                .id(1L)
                .build();

        EventoResponseDTO responseDTO = EventoResponseDTO.builder()
                .id(1L)
                .titulo("New Evento")
                .descripcion("New Description")
                .organizador(orgDTO)
                .build();

        when(organizadorService.findById(1L)).thenReturn(organizador);
        when(eventoMapper.toEntity(any(EventoRequestDTO.class), any(Organizador.class))).thenReturn(evento);
        when(eventoService.save(any(Evento.class))).thenReturn(savedEvento);
        when(eventoMapper.toResponseDTO(savedEvento)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("New Evento"));

        verify(eventoService, times(1)).save(any(Evento.class));
    }

    @Test
    @DisplayName("POST /api/v1/eventos - Should return 400 for invalid data")
    void testCreateEventoInvalidData() throws Exception {
        // Given
        EventoRequestDTO requestDTO = EventoRequestDTO.builder()
                .titulo("") // Invalid: blank
                .descripcion("Short") // Invalid: too short
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/eventos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").exists());

        verify(eventoService, never()).save(any());
    }

    @Test
    @DisplayName("PUT /api/v1/eventos/{id} - Should update evento successfully")
    void testUpdateEvento() throws Exception {
        // Given
        Long id = 1L;
        EventoRequestDTO requestDTO = EventoRequestDTO.builder()
                .titulo("Updated Evento")
                .descripcion("Updated Description")
                .fecha(LocalDateTime.now().plusDays(30))
                .ubicacion("Barcelona")
                .organizadorId(1L)
                .build();

        Organizador organizador = new Organizador();
        organizador.setId(1L);

        Evento evento = new Evento();
        evento.setTitulo("Updated Evento");

        Evento updatedEvento = new Evento();
        updatedEvento.setId(id);
        updatedEvento.setTitulo("Updated Evento");
        updatedEvento.setOrganizador(organizador);

        OrganizadorResponseDTO orgDTO = OrganizadorResponseDTO.builder()
                .id(1L)
                .build();

        EventoResponseDTO responseDTO = EventoResponseDTO.builder()
                .id(id)
                .titulo("Updated Evento")
                .organizador(orgDTO)
                .build();

        when(organizadorService.findById(1L)).thenReturn(organizador);
        when(eventoMapper.toEntity(any(EventoRequestDTO.class), any(Organizador.class))).thenReturn(evento);
        when(eventoService.update(eq(id), any(Evento.class))).thenReturn(updatedEvento);
        when(eventoMapper.toResponseDTO(updatedEvento)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/eventos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Updated Evento"));

        verify(eventoService, times(1)).update(eq(id), any(Evento.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/eventos/{id} - Should delete evento successfully")
    void testDeleteEvento() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(eventoService).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/eventos/{id}", id))
                .andExpect(status().isNoContent());

        verify(eventoService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("GET /api/v1/eventos/search - Should search by titulo")
    void testSearchByTitulo() throws Exception {
        // Given
        String titulo = "Java";
        Organizador org = new Organizador();
        org.setId(1L);

        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setTitulo("Java Conference");
        evento1.setOrganizador(org);

        OrganizadorResponseDTO orgDTO = OrganizadorResponseDTO.builder()
                .id(1L)
                .build();

        EventoResponseDTO dto1 = EventoResponseDTO.builder()
                .id(1L)
                .titulo("Java Conference")
                .organizador(orgDTO)
                .build();

        when(eventoService.searchByTitulo(titulo)).thenReturn(Arrays.asList(evento1));
        when(eventoMapper.toResponseDTO(evento1)).thenReturn(dto1);

        // When & Then
        mockMvc.perform(get("/api/v1/eventos/search")
                        .param("titulo", titulo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Java Conference"));

        verify(eventoService, times(1)).searchByTitulo(titulo);
    }

    @Test
    @DisplayName("GET /api/v1/eventos/echo - Should return echo")
    void testEcho() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/eventos/echo"))
                .andExpect(status().isOk())
                .andExpect(content().string("echo"));
    }
}
