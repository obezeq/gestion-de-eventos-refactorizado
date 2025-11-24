package org.example.apirest.service;

import org.example.apirest.entity.Organizador;
import org.example.apirest.exception.DuplicateResourceException;
import org.example.apirest.exception.ResourceNotFoundException;
import org.example.apirest.repository.OrganizadorRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizadorService Tests")
class OrganizadorServiceTest {

    @Mock
    private OrganizadorRepository organizadorRepository;

    @InjectMocks
    private OrganizadorService organizadorService;

    @Test
    @DisplayName("Should find all organizadores")
    void testFindAll() {
        // Given
        Organizador org1 = new Organizador();
        org1.setId(1L);
        org1.setNombre("Org 1");
        org1.setEmail("org1@test.com");

        Organizador org2 = new Organizador();
        org2.setId(2L);
        org2.setNombre("Org 2");
        org2.setEmail("org2@test.com");

        List<Organizador> organizadores = Arrays.asList(org1, org2);
        when(organizadorRepository.findAll()).thenReturn(organizadores);

        // When
        List<Organizador> result = organizadorService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(organizadorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find all organizadores with pagination")
    void testFindAllWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Organizador org1 = new Organizador();
        org1.setId(1L);
        org1.setNombre("Org 1");
        org1.setEmail("org1@test.com");

        List<Organizador> organizadores = Arrays.asList(org1);
        Page<Organizador> page = new PageImpl<>(organizadores, pageable, 1);
        when(organizadorRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Organizador> result = organizadorService.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(organizadorRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Should find organizador by id successfully")
    void testFindById() {
        // Given
        Long id = 1L;
        Organizador organizador = new Organizador();
        organizador.setId(id);
        organizador.setNombre("Test Organizador");
        organizador.setEmail("test@test.com");
        when(organizadorRepository.findById(id)).thenReturn(Optional.of(organizador));

        // When
        Organizador result = organizadorService.findById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNombre()).isEqualTo("Test Organizador");
        verify(organizadorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when organizador not found")
    void testFindByIdNotFound() {
        // Given
        Long id = 999L;
        when(organizadorRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> organizadorService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Organizador")
                .hasMessageContaining("id")
                .hasMessageContaining("999");
        verify(organizadorRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should save organizador successfully")
    void testSave() {
        // Given
        Organizador organizador = new Organizador();
        organizador.setNombre("New Organizador");
        organizador.setEmail("new@test.com");

        Organizador savedOrganizador = new Organizador();
        savedOrganizador.setId(1L);
        savedOrganizador.setNombre("New Organizador");
        savedOrganizador.setEmail("new@test.com");

        when(organizadorRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(organizadorRepository.save(any(Organizador.class))).thenReturn(savedOrganizador);

        // When
        Organizador result = organizadorService.save(organizador);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(organizadorRepository, times(1)).findByEmail("new@test.com");
        verify(organizadorRepository, times(1)).save(organizador);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void testSaveDuplicateEmail() {
        // Given
        Organizador existing = new Organizador();
        existing.setId(1L);
        existing.setEmail("duplicate@test.com");

        Organizador newOrganizador = new Organizador();
        newOrganizador.setEmail("duplicate@test.com");

        when(organizadorRepository.findByEmail("duplicate@test.com"))
                .thenReturn(Optional.of(existing));

        // When & Then
        assertThatThrownBy(() -> organizadorService.save(newOrganizador))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email")
                .hasMessageContaining("duplicate@test.com");
        verify(organizadorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update organizador successfully")
    void testUpdate() {
        // Given
        Long id = 1L;
        Organizador existing = new Organizador();
        existing.setId(id);
        existing.setNombre("Old Name");
        existing.setEmail("old@test.com");

        Organizador updated = new Organizador();
        updated.setNombre("New Name");
        updated.setEmail("new@test.com");
        updated.setTelefono("123456789");

        when(organizadorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(organizadorRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(organizadorRepository.save(any(Organizador.class))).thenReturn(existing);

        // When
        Organizador result = organizadorService.update(id, updated);

        // Then
        assertThat(result.getNombre()).isEqualTo("New Name");
        assertThat(result.getEmail()).isEqualTo("new@test.com");
        verify(organizadorRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should delete organizador by id successfully")
    void testDeleteById() {
        // Given
        Long id = 1L;
        when(organizadorRepository.existsById(id)).thenReturn(true);
        doNothing().when(organizadorRepository).deleteById(id);

        // When
        organizadorService.deleteById(id);

        // Then
        verify(organizadorRepository, times(1)).existsById(id);
        verify(organizadorRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent organizador")
    void testDeleteByIdNotFound() {
        // Given
        Long id = 999L;
        when(organizadorRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> organizadorService.deleteById(id))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(organizadorRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Should search organizadores by nombre")
    void testSearchByNombre() {
        // Given
        String nombre = "Carlos";
        Organizador org1 = new Organizador();
        org1.setId(1L);
        org1.setNombre("Carlos Ruiz");

        Organizador org2 = new Organizador();
        org2.setId(2L);
        org2.setNombre("Juan Carlos");

        List<Organizador> organizadores = Arrays.asList(org1, org2);
        when(organizadorRepository.findByNombreContainingIgnoreCase(nombre))
                .thenReturn(organizadores);

        // When
        List<Organizador> result = organizadorService.searchByNombre(nombre);

        // Then
        assertThat(result).hasSize(2);
        verify(organizadorRepository, times(1)).findByNombreContainingIgnoreCase(nombre);
    }
}
