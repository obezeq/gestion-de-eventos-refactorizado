package org.example.apirest.repository;

import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("EventoRepository Tests")
class EventoRepositoryTest {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Organizador organizador;

    @BeforeEach
    void setUp() {
        organizador = new Organizador();
        organizador.setNombre("Test Organizador");
        organizador.setEmail("org@test.com");
        entityManager.persistAndFlush(organizador);
    }

    @Test
    @DisplayName("Should save evento successfully")
    void testSaveEvento() {
        // Given
        Evento evento = new Evento();
        evento.setTitulo("Conferencia Spring Boot");
        evento.setDescripcion("Evento sobre Spring Boot avanzado");
        evento.setFecha(LocalDateTime.now().plusDays(30));
        evento.setUbicacion("Madrid");
        evento.setOrganizador(organizador);

        // When
        Evento savedEvento = eventoRepository.save(evento);

        // Then
        assertThat(savedEvento).isNotNull();
        assertThat(savedEvento.getId()).isNotNull();
        assertThat(savedEvento.getTitulo()).isEqualTo("Conferencia Spring Boot");
    }

    @Test
    @DisplayName("Should find evento by id")
    void testFindById() {
        // Given
        Evento evento = new Evento();
        evento.setTitulo("Workshop Java");
        evento.setDescripcion("Taller práctico de Java");
        evento.setOrganizador(organizador);
        entityManager.persistAndFlush(evento);

        // When
        Optional<Evento> found = eventoRepository.findById(evento.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitulo()).isEqualTo("Workshop Java");
    }

    @Test
    @DisplayName("Should find eventos by titulo containing ignore case")
    void testFindByTituloContainingIgnoreCase() {
        // Given
        Evento evento1 = new Evento();
        evento1.setTitulo("Java Conference 2024");
        evento1.setDescripcion("Descripcion 1");
        evento1.setOrganizador(organizador);

        Evento evento2 = new Evento();
        evento2.setTitulo("Spring Boot java Workshop");
        evento2.setDescripcion("Descripcion 2");
        evento2.setOrganizador(organizador);

        entityManager.persist(evento1);
        entityManager.persist(evento2);
        entityManager.flush();

        // When
        List<Evento> found = eventoRepository.findByTituloContainingIgnoreCase("java");

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Evento::getTitulo)
                .containsExactlyInAnyOrder("Java Conference 2024", "Spring Boot java Workshop");
    }

    @Test
    @DisplayName("Should delete evento by id")
    void testDeleteById() {
        // Given
        Evento evento = new Evento();
        evento.setTitulo("Evento a eliminar");
        evento.setDescripcion("Descripcion");
        evento.setOrganizador(organizador);
        entityManager.persistAndFlush(evento);
        Long id = evento.getId();

        // When
        eventoRepository.deleteById(id);
        Optional<Evento> found = eventoRepository.findById(id);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should cascade delete participantes when evento is deleted")
    void testCascadeDeleteParticipantes() {
        // Given
        Evento evento = new Evento();
        evento.setTitulo("Evento con participantes");
        evento.setDescripcion("Test cascade");
        evento.setOrganizador(organizador);
        entityManager.persistAndFlush(evento);

        Long eventoId = evento.getId();

        // When
        eventoRepository.deleteById(eventoId);
        entityManager.flush();
        Optional<Evento> found = eventoRepository.findById(eventoId);

        // Then
        assertThat(found).isEmpty();
    }
}
