package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

public class Sensor {
    @Id
    private long id;

    @Column(name="valor")
    private int valor;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private String tipo;

}
