package org.example.apirest.service;

import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.repository.EventoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EventoService Tests")
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    @Test
    @DisplayName("Should find all eventos")
    void testFindAll() {
        // Given
        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setTitulo("Evento 1");

        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setTitulo("Evento 2");

        List<Evento> eventos = Arrays.asList(evento1, evento2);
        when(eventoRepository.findAll()).thenReturn(eventos);

        // When
        List<Evento> result = eventoService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find all eventos with pagination")
    void testFindAllWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setTitulo("Evento 1");

        List<Evento> eventos = Arrays.asList(evento1);
        Page<Evento> page = new PageImpl<>(eventos, pageable, 1);
        when(eventoRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Evento> result = eventoService.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        verify(eventoRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should find evento by id successfully")
    void testFindById() {
        // Given
        Long id = 1L;
        Evento evento = new Evento();
        evento.setId(id);
        evento.setTitulo("Test Evento");
        evento.setDescripcion("Descripcion test");
        when(eventoRepository.findById(id)).thenReturn(Optional.of(evento));

        // When
        Evento result = eventoService.findById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(eventoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when evento not found")
    void testFindByIdNotFound() {
        // Given
        Long id = 999L;
        when(eventoRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> eventoService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Evento");
        verify(eventoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should save evento successfully")
    void testSave() {
        // Given
        Organizador organizador = new Organizador();
        organizador.setId(1L);

        Evento evento = new Evento();
        evento.setTitulo("New Evento");
        evento.setDescripcion("Descripcion");
        evento.setOrganizador(organizador);

        Evento savedEvento = new Evento();
        savedEvento.setId(1L);
        savedEvento.setTitulo("New Evento");
        savedEvento.setDescripcion("Descripcion");
        savedEvento.setOrganizador(organizador);

        when(eventoRepository.save(any(Evento.class))).thenReturn(savedEvento);

        // When
        Evento result = eventoService.save(evento);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(eventoRepository, times(1)).save(evento);
    }

    @Test
    @DisplayName("Should update evento successfully")
    void testUpdate() {
        // Given
        Long id = 1L;
        Organizador organizador = new Organizador();
        organizador.setId(1L);

        Evento existing = new Evento();
        existing.setId(id);
        existing.setTitulo("Old Title");
        existing.setDescripcion("Old Desc");

        Evento updated = new Evento();
        updated.setTitulo("New Title");
        updated.setDescripcion("New Desc");
        updated.setFecha(LocalDateTime.now().plusDays(10));
        updated.setUbicacion("Madrid");
        updated.setOrganizador(organizador);

        when(eventoRepository.findById(id)).thenReturn(Optional.of(existing));
        when(eventoRepository.save(any(Evento.class))).thenReturn(existing);

        // When
        Evento result = eventoService.update(id, updated);

        // Then
        assertThat(result.getTitulo()).isEqualTo("New Title");
        verify(eventoRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should delete evento by id successfully")
    void testDeleteById() {
        // Given
        Long id = 1L;
        when(eventoRepository.existsById(id)).thenReturn(true);
        doNothing().when(eventoRepository).deleteById(id);

        // When
        eventoService.deleteById(id);

        // Then
        verify(eventoRepository, times(1)).existsById(id);
        verify(eventoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should search eventos by titulo")
    void testSearchByTitulo() {
        // Given
        String titulo = "Java";
        Evento evento1 = new Evento();
        evento1.setId(1L);
        evento1.setTitulo("Java Conference");

        Evento evento2 = new Evento();
        evento2.setId(2L);
        evento2.setTitulo("Spring Boot Java");

        List<Evento> eventos = Arrays.asList(evento1, evento2);
        when(eventoRepository.findByTituloContainingIgnoreCase(titulo))
                .thenReturn(eventos);

        // When
        List<Evento> result = eventoService.searchByTitulo(titulo);

        // Then
        assertThat(result).hasSize(2);
        verify(eventoRepository, times(1)).findByTituloContainingIgnoreCase(titulo);
    }
}
