package org.example.apirest.service;

import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Participante;
import org.example.apirest.exception.DuplicateResourceException;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.repository.ParticipanteRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParticipanteService Tests")
class ParticipanteServiceTest {

    @Mock
    private ParticipanteRepository participanteRepository;

    @InjectMocks
    private ParticipanteService participanteService;

    @Test
    @DisplayName("Should find all participantes")
    void testFindAll() {
        // Given
        Participante p1 = new Participante();
        p1.setId(1L);
        p1.setNombre("Part 1");

        Participante p2 = new Participante();
        p2.setId(2L);
        p2.setNombre("Part 2");

        List<Participante> participantes = Arrays.asList(p1, p2);
        when(participanteRepository.findAll()).thenReturn(participantes);

        // When
        List<Participante> result = participanteService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(participanteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find all participantes with pagination")
    void testFindAllWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Participante p1 = new Participante();
        p1.setId(1L);
        p1.setNombre("Part 1");

        List<Participante> participantes = Arrays.asList(p1);
        Page<Participante> page = new PageImpl<>(participantes, pageable, 1);
        when(participanteRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Participante> result = participanteService.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        verify(participanteRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should find participante by id successfully")
    void testFindById() {
        // Given
        Long id = 1L;
        Participante participante = new Participante();
        participante.setId(id);
        participante.setNombre("Test Participante");
        participante.setEmail("test@test.com");
        when(participanteRepository.findById(id)).thenReturn(Optional.of(participante));

        // When
        Participante result = participanteService.findById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(participanteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when participante not found")
    void testFindByIdNotFound() {
        // Given
        Long id = 999L;
        when(participanteRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> participanteService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Participante");
        verify(participanteRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should save participante successfully")
    void testSave() {
        // Given
        Evento evento = new Evento();
        evento.setId(1L);

        Participante participante = new Participante();
        participante.setNombre("New Participante");
        participante.setEmail("new@test.com");
        participante.setEvento(evento);

        Participante savedParticipante = new Participante();
        savedParticipante.setId(1L);
        savedParticipante.setNombre("New Participante");
        savedParticipante.setEmail("new@test.com");
        savedParticipante.setEvento(evento);

        when(participanteRepository.findByEmailAndEventoId(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(participanteRepository.save(any(Participante.class))).thenReturn(savedParticipante);

        // When
        Participante result = participanteService.save(participante);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(participanteRepository, times(1)).save(participante);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email exists in evento")
    void testSaveDuplicateEmailInEvento() {
        // Given
        Evento evento = new Evento();
        evento.setId(1L);

        Participante existing = new Participante();
        existing.setId(1L);
        existing.setEmail("duplicate@test.com");
        existing.setEvento(evento);

        Participante newParticipante = new Participante();
        newParticipante.setEmail("duplicate@test.com");
        newParticipante.setEvento(evento);

        when(participanteRepository.findByEmailAndEventoId("duplicate@test.com", 1L))
                .thenReturn(Optional.of(existing));

        // When & Then
        assertThatThrownBy(() -> participanteService.save(newParticipante))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
        verify(participanteRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should find participantes by evento id")
    void testFindByEventoId() {
        // Given
        Long eventoId = 1L;
        Participante p1 = new Participante();
        p1.setId(1L);
        p1.setNombre("Part 1");

        Participante p2 = new Participante();
        p2.setId(2L);
        p2.setNombre("Part 2");

        List<Participante> participantes = Arrays.asList(p1, p2);
        when(participanteRepository.findByEventoId(eventoId)).thenReturn(participantes);

        // When
        List<Participante> result = participanteService.findByEventoId(eventoId);

        // Then
        assertThat(result).hasSize(2);
        verify(participanteRepository, times(1)).findByEventoId(eventoId);
    }

    @Test
    @DisplayName("Should find participantes by evento id with pagination")
    void testFindByEventoIdWithPagination() {
        // Given
        Long eventoId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Participante p1 = new Participante();
        p1.setId(1L);
        p1.setNombre("Part 1");

        List<Participante> participantes = Arrays.asList(p1);
        Page<Participante> page = new PageImpl<>(participantes, pageable, 1);
        when(participanteRepository.findByEventoId(eventoId, pageable)).thenReturn(page);

        // When
        Page<Participante> result = participanteService.findByEventoId(eventoId, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        verify(participanteRepository, times(1)).findByEventoId(eventoId, pageable);
    }
}
