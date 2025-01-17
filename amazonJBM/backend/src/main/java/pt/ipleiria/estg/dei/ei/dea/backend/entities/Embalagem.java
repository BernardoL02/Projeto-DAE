package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Embalagem {

    @Id
    private int id;

    @ManyToOne
    @NotNull
    private Produto produto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_volume", nullable = false)
    private Volume volume;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "embalagem", cascade = CascadeType.ALL)
    private List<Sensor> sensores = new ArrayList<>();

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private Tipo_Embalagem tipo;

    public Embalagem(int id,Produto produto, Volume volume, int quantidade, Tipo_Embalagem tipo) {
        this.id = id;
        this.produto = produto;
        this.volume = volume;
        this.quantidade = quantidade;
        this.tipo = tipo;
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

    public Tipo_Embalagem getTipo() {
        return tipo;
    }

    public void setTipo(Tipo_Embalagem tipo) {
        this.tipo = tipo;
    }

    public boolean sensorExiste(Sensor sensor) {
        return sensores.contains(sensor);
    }

    public void removerSensor(Sensor sensor) {
        sensores.remove(sensor);
    }
}
