package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Embalagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private Tipo_Embalagem tipo;

    public Embalagem( Tipo_Embalagem tipo) {
        this.tipo = tipo;
    }

    public Embalagem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tipo_Embalagem getTipo() {
        return tipo;
    }

    public void setTipo(Tipo_Embalagem tipo) {
        this.tipo = tipo;
    }
}
