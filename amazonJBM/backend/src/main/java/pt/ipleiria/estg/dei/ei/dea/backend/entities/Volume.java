package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;


@Entity

public class Volume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @ManyToOne
    @NotNull
    private  Encomenda encomenda;

    private int quantidade;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "volume")
    private List<Sensor> sensores = new ArrayList<>();

    public Volume(Produto produto, int quantidade, Encomenda encomenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.encomenda = encomenda;

    }

    public Volume(){

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

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public @NotNull Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(@NotNull Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public void addSensor(Sensor sensor) {
        sensores.add(sensor);

    }

    public List<Sensor> getSensores() {
        return sensores;
    }

}
