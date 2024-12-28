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
    @NotNull
    private Encomenda encomenda;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "volume", cascade = CascadeType.ALL)
    private List<Embalagem> embalagens = new ArrayList<>();

    public Volume(Encomenda encomenda) {
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

