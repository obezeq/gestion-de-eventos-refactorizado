package org.example.apirest.repository;

import org.example.apirest.entity.Organizador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("OrganizadorRepository Tests")
class OrganizadorRepositoryTest {

    @Autowired
    private OrganizadorRepository organizadorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Should save organizador successfully")
    void testSaveOrganizador() {
        // Given
        Organizador organizador = new Organizador();
        organizador.setNombre("Juan Pérez");
        organizador.setEmail("juan@test.com");
        organizador.setTelefono("123456789");

        // When
        Organizador savedOrganizador = organizadorRepository.save(organizador);

        // Then
        assertThat(savedOrganizador).isNotNull();
        assertThat(savedOrganizador.getId()).isNotNull();
        assertThat(savedOrganizador.getNombre()).isEqualTo("Juan Pérez");
        assertThat(savedOrganizador.getEmail()).isEqualTo("juan@test.com");
    }

    @Test
    @DisplayName("Should find organizador by id")
    void testFindById() {
        // Given
        Organizador organizador = new Organizador();
        organizador.setNombre("María López");
        organizador.setEmail("maria@test.com");
        entityManager.persistAndFlush(organizador);

        // When
        Optional<Organizador> found = organizadorRepository.findById(organizador.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("María López");
    }

    @Test
    @DisplayName("Should find organizador by email")
    void testFindByEmail() {
        // Given
        Organizador organizador = new Organizador();
        organizador.setNombre("Carlos Ruiz");
        organizador.setEmail("carlos@test.com");
        entityManager.persistAndFlush(organizador);

        // When
        Optional<Organizador> found = organizadorRepository.findByEmail("carlos@test.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getNombre()).isEqualTo("Carlos Ruiz");
    }

    @Test
    @DisplayName("Should find organizadores by nombre containing ignore case")
    void testFindByNombreContainingIgnoreCase() {
        // Given
        Organizador org1 = new Organizador();
        org1.setNombre("Juan Carlos");
        org1.setEmail("juanc@test.com");

        Organizador org2 = new Organizador();
        org2.setNombre("Carlos Alberto");
        org2.setEmail("carlosa@test.com");

        entityManager.persist(org1);
        entityManager.persist(org2);
        entityManager.flush();

        // When
        List<Organizador> found = organizadorRepository.findByNombreContainingIgnoreCase("carlos");

        // Then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Organizador::getNombre)
                .containsExactlyInAnyOrder("Juan Carlos", "Carlos Alberto");
    }

    @Test
    @DisplayName("Should delete organizador by id")
    void testDeleteById() {
        // Given
        Organizador organizador = new Organizador();
        organizador.setNombre("Pedro Sánchez");
        organizador.setEmail("pedro@test.com");
        entityManager.persistAndFlush(organizador);
        Long id = organizador.getId();

        // When
        organizadorRepository.deleteById(id);
        Optional<Organizador> found = organizadorRepository.findById(id);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindByEmailNotFound() {
        // When
        Optional<Organizador> found = organizadorRepository.findByEmail("notfound@test.com");

        // Then
        assertThat(found).isEmpty();
    }
}
