package org.example.apirest.repository;

import org.example.apirest.entity.Evento;
import org.example.apirest.entity.Organizador;
import org.example.apirest.entity.Participante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("ParticipanteRepository Tests")
class ParticipanteRepositoryTest {

    @Autowired
    private ParticipanteRepository participanteRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Evento evento;

    @BeforeEach
    void setUp() {
        Organizador organizador = new Organizador();
        organizador.setNombre("Test Organizador");
        organizador.setEmail("org@test.com");
        entityManager.persist(organizador);

        evento = new Evento();
        evento.setTitulo("Test Evento");
        evento.setDescripcion("Descripcion test");
        evento.setOrganizador(organizador);
        entityManager.persistAndFlush(evento);
    }

    @Test
    @DisplayName("Should save participante successfully")
    void testSaveParticipante() {
        // Given
        Participante participante = new Participante();
        participante.setNombre("Ana García");
        participante.setEmail("ana@test.com");
        participante.setTelefono("987654321");
        participante.setEvento(evento);

        // When
        Participante savedParticipante = participanteRepository.save(participante);

        // Then
        assertThat(savedParticipante).isNotNull();
        assertThat(savedParticipante.getId()).isNotNull();
        assertThat(savedParticipante.getNombre()).isEqualTo("Ana García");
    }

    @Test
    @DisplayName("Should find participante by email containing ignore case")
    void testFindByEmailContainingIgnoreCase() {
        // Given
        Participante p1 = new Participante();
        p1.setNombre("Test 1");
        p1.setEmail("test1@gmail.com");
        p1.setEvento(evento);

        Participante p2 = new Participante();
        p2.setNombre("Test 2");
        p2.setEmail("test2@gmail.com");
        p2.setEvento(evento);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();

        // When
        List<Participante> found = participanteRepository.findByEmailContainingIgnoreCase("gmail");

        // Then
        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("Should find participantes by evento id")
    void testFindByEventoId() {
        // Given
        Participante p1 = new Participante();
        p1.setNombre("Part 1");
        p1.setEmail("p1@test.com");
        p1.setEvento(evento);

        Participante p2 = new Participante();
        p2.setNombre("Part 2");
        p2.setEmail("p2@test.com");
        p2.setEvento(evento);

        entityManager.persist(p1);
        entityManager.persist(p2);
        entityManager.flush();

        // When
        List<Participante> found = participanteRepository.findByEventoId(evento.getId());

        // Then
        assertThat(found).hasSize(2);
    }

    @Test
    @DisplayName("Should find participantes by evento id with pagination")
    void testFindByEventoIdWithPagination() {
        // Given
        for (int i = 1; i <= 5; i++) {
            Participante p = new Participante();
            p.setNombre("Part " + i);
            p.setEmail("p" + i + "@test.com");
            p.setEvento(evento);
            entityManager.persist(p);
        }
        entityManager.flush();

        // When
        Page<Participante> page = participanteRepository.findByEventoId(
                evento.getId(), PageRequest.of(0, 3));

        // Then
        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find participante by email and evento id")
    void testFindByEmailAndEventoId() {
        // Given
        Participante participante = new Participante();
        participante.setNombre("Unique Participant");
        participante.setEmail("unique@test.com");
        participante.setEvento(evento);
        entityManager.persistAndFlush(participante);

        // When
        Optional<Participante> found = participanteRepository
                .findByEmailAndEventoId("unique@test.com", evento.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Unique Participant");
    }

    @Test
    @DisplayName("Should return empty when participante not found by email and evento")
    void testFindByEmailAndEventoIdNotFound() {
        // When
        Optional<Participante> found = participanteRepository
                .findByEmailAndEventoId("notfound@test.com", evento.getId());

        // Then
        assertThat(found).isEmpty();
    }
}
