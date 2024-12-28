package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Embalagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @NotNull
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "id_volume", nullable = false)
    private Volume volume;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "embalagem", cascade = CascadeType.ALL)
    private List<Sensor> sensores = new ArrayList<>();

    private int quantidade;

    public Embalagem(Produto produto, Volume volume, int quantidade) {
        this.produto = produto;
        this.volume = volume;
        this.quantidade = quantidade;
    }

    public Embalagem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);
        sensor.setEmbalagem(this);
    }
}
