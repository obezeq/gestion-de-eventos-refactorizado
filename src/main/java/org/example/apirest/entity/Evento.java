package org.example.apirest.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private Long id;

    @Column (nullable = false)
    private String titulo;

    @Column (nullable = false)
    private String descripcion;

    @Column (nullable = true)
    private LocalDateTime fecha;

    @Column (nullable = true)
    private String ubicacion;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Participante> participantes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizador_id", nullable = false)
    @JsonBackReference(value = "organizador-eventos")
    private Organizador organizador;

    public void addParticipante(Participante participante) {
        participantes.add(participante);
        participante.setEvento(this);
    }

    public void removeParticipante(Participante participante) {
        participantes.remove(participante);
        participante.setEvento(null);
    }

}
