package org.example.apirest.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe de ser válido")
    private String email;

    @Column(nullable = true)
    private String telefono;

    @OneToMany(mappedBy = "organizador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "organizador-eventos")
    private List<Evento> eventos = new ArrayList<>();

    public void addEvento(Evento evento) {
        eventos.add(evento);
        evento.setOrganizador(this);
    }

    public void removeEvento(Evento evento) {
        eventos.remove(evento);
        evento.setOrganizador(null);
    }
}
