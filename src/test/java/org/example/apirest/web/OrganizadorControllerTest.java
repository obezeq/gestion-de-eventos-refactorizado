package org.example.apirest.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apirest.dto.organizador.OrganizadorRequestDTO;
import org.example.apirest.dto.organizador.OrganizadorResponseDTO;
import org.example.apirest.entity.Organizador;
import org.example.apirest.exception.DuplicateResourceException;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.mapper.EventoMapper;
import org.example.apirest.mapper.OrganizadorMapper;
import org.example.apirest.service.OrganizadorService;
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

@WebMvcTest(OrganizadorController.class)
@DisplayName("OrganizadorController Tests")
class OrganizadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrganizadorService organizadorService;

    @MockBean
    private OrganizadorMapper organizadorMapper;

    @MockBean
    private EventoMapper eventoMapper;

    @Test
    @DisplayName("GET /api/v1/organizadores - Should return page of organizadores")
    void testGetAllOrganizadores() throws Exception {
        // Given
        Organizador org1 = new Organizador();
        org1.setId(1L);
        org1.setNombre("Org 1");
        org1.setEmail("org1@test.com");

        OrganizadorResponseDTO dto1 = OrganizadorResponseDTO.builder()
                .id(1L)
                .nombre("Org 1")
                .email("org1@test.com")
                .build();

        List<Organizador> organizadores = Arrays.asList(org1);
        Page<Organizador> page = new PageImpl<>(organizadores, PageRequest.of(0, 10), 1);

        when(organizadorService.findAll(any(PageRequest.class))).thenReturn(page);
        when(organizadorMapper.toResponseDTO(org1)).thenReturn(dto1);

        // When & Then
        mockMvc.perform(get("/api/v1/organizadores")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].nombre").value("Org 1"));

        verify(organizadorService, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/organizadores/{id} - Should return organizador by id")
    void testGetOrganizadorById() throws Exception {
        // Given
        Long id = 1L;
        Organizador organizador = new Organizador();
        organizador.setId(id);
        organizador.setNombre("Test Org");
        organizador.setEmail("test@test.com");

        OrganizadorResponseDTO dto = OrganizadorResponseDTO.builder()
                .id(id)
                .nombre("Test Org")
                .email("test@test.com")
                .build();

        when(organizadorService.findById(id)).thenReturn(organizador);
        when(organizadorMapper.toResponseDTO(organizador)).thenReturn(dto);

        // When & Then
        mockMvc.perform(get("/api/v1/organizadores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Test Org"))
                .andExpect(jsonPath("$.email").value("test@test.com"));

        verify(organizadorService, times(1)).findById(id);
    }

    @Test
    @DisplayName("GET /api/v1/organizadores/{id} - Should return 404 when not found")
    void testGetOrganizadorByIdNotFound() throws Exception {
        // Given
        Long id = 999L;
        when(organizadorService.findById(id))
                .thenThrow(new ResourceNotFoundException("Organizador", "id", id));

        // When & Then
        mockMvc.perform(get("/api/v1/organizadores/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organizador no encontrado con id: '999'"));

        verify(organizadorService, times(1)).findById(id);
    }

    @Test
    @DisplayName("POST /api/v1/organizadores - Should create organizador successfully")
    void testCreateOrganizador() throws Exception {
        // Given
        OrganizadorRequestDTO requestDTO = OrganizadorRequestDTO.builder()
                .nombre("New Org")
                .email("new@test.com")
                .telefono("123456789")
                .build();

        Organizador organizador = new Organizador();
        organizador.setNombre("New Org");
        organizador.setEmail("new@test.com");
        organizador.setTelefono("123456789");

        Organizador savedOrganizador = new Organizador();
        savedOrganizador.setId(1L);
        savedOrganizador.setNombre("New Org");
        savedOrganizador.setEmail("new@test.com");
        savedOrganizador.setTelefono("123456789");

        OrganizadorResponseDTO responseDTO = OrganizadorResponseDTO.builder()
                .id(1L)
                .nombre("New Org")
                .email("new@test.com")
                .telefono("123456789")
                .build();

        when(organizadorMapper.toEntity(any(OrganizadorRequestDTO.class))).thenReturn(organizador);
        when(organizadorService.save(any(Organizador.class))).thenReturn(savedOrganizador);
        when(organizadorMapper.toResponseDTO(savedOrganizador)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("New Org"))
                .andExpect(jsonPath("$.email").value("new@test.com"));

        verify(organizadorService, times(1)).save(any(Organizador.class));
    }

    @Test
    @DisplayName("POST /api/v1/organizadores - Should return 400 for invalid data")
    void testCreateOrganizadorInvalidData() throws Exception {
        // Given
        OrganizadorRequestDTO requestDTO = OrganizadorRequestDTO.builder()
                .nombre("") // Invalid: blank
                .email("invalid-email") // Invalid: not email format
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors").exists());

        verify(organizadorService, never()).save(any());
    }

    @Test
    @DisplayName("POST /api/v1/organizadores - Should return 409 for duplicate email")
    void testCreateOrganizadorDuplicateEmail() throws Exception {
        // Given
        OrganizadorRequestDTO requestDTO = OrganizadorRequestDTO.builder()
                .nombre("New Org")
                .email("duplicate@test.com")
                .build();

        Organizador organizador = new Organizador();
        organizador.setNombre("New Org");
        organizador.setEmail("duplicate@test.com");

        when(organizadorMapper.toEntity(any(OrganizadorRequestDTO.class))).thenReturn(organizador);
        when(organizadorService.save(any(Organizador.class)))
                .thenThrow(new DuplicateResourceException("Organizador", "email", "duplicate@test.com"));

        // When & Then
        mockMvc.perform(post("/api/v1/organizadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Organizador ya existe con email: 'duplicate@test.com'"));

        verify(organizadorService, times(1)).save(any(Organizador.class));
    }

    @Test
    @DisplayName("PUT /api/v1/organizadores/{id} - Should update organizador successfully")
    void testUpdateOrganizador() throws Exception {
        // Given
        Long id = 1L;
        OrganizadorRequestDTO requestDTO = OrganizadorRequestDTO.builder()
                .nombre("Updated Org")
                .email("updated@test.com")
                .telefono("987654321")
                .build();

        Organizador organizador = new Organizador();
        organizador.setNombre("Updated Org");
        organizador.setEmail("updated@test.com");

        Organizador updatedOrganizador = new Organizador();
        updatedOrganizador.setId(id);
        updatedOrganizador.setNombre("Updated Org");
        updatedOrganizador.setEmail("updated@test.com");

        OrganizadorResponseDTO responseDTO = OrganizadorResponseDTO.builder()
                .id(id)
                .nombre("Updated Org")
                .email("updated@test.com")
                .build();

        when(organizadorMapper.toEntity(any(OrganizadorRequestDTO.class))).thenReturn(organizador);
        when(organizadorService.update(eq(id), any(Organizador.class))).thenReturn(updatedOrganizador);
        when(organizadorMapper.toResponseDTO(updatedOrganizador)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/organizadores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Updated Org"));

        verify(organizadorService, times(1)).update(eq(id), any(Organizador.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/organizadores/{id} - Should delete organizador successfully")
    void testDeleteOrganizador() throws Exception {
        // Given
        Long id = 1L;
        doNothing().when(organizadorService).deleteById(id);

        // When & Then
        mockMvc.perform(delete("/api/v1/organizadores/{id}", id))
                .andExpect(status().isNoContent());

        verify(organizadorService, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("GET /api/v1/organizadores/search - Should search by nombre")
    void testSearchByNombre() throws Exception {
        // Given
        String nombre = "Carlos";
        Organizador org1 = new Organizador();
        org1.setId(1L);
        org1.setNombre("Carlos Ruiz");
        org1.setEmail("carlos@test.com");

        OrganizadorResponseDTO dto1 = OrganizadorResponseDTO.builder()
                .id(1L)
                .nombre("Carlos Ruiz")
                .email("carlos@test.com")
                .build();

        when(organizadorService.searchByNombre(nombre)).thenReturn(Arrays.asList(org1));
        when(organizadorMapper.toResponseDTO(org1)).thenReturn(dto1);

        // When & Then
        mockMvc.perform(get("/api/v1/organizadores/search")
                        .param("nombre", nombre))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Carlos Ruiz"));

        verify(organizadorService, times(1)).searchByNombre(nombre);
    }
}
