package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "Volume")
public class Volume {

    @Id
    private int id;

    private boolean entregue = false;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "encomenda_id")
    private Encomenda encomenda;

    @OneToMany(mappedBy = "volume", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Embalagem> embalagens = new ArrayList<>();

    public Volume(int id,Encomenda encomenda) {
        this.id = id;
        this.encomenda = encomenda;

        if(embalagens == null){
            embalagens = new ArrayList<>();
        }
    }

    public Volume() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public @NotNull Encomenda getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(@NotNull Encomenda encomenda) {
        this.encomenda = encomenda;
    }

    public List<Embalagem> getEmbalagens() {
        return embalagens;
    }


    public void addEmbalagem(Embalagem embalagem) {
        embalagens.add(embalagem);
        embalagem.setVolume(this);
    }
}

