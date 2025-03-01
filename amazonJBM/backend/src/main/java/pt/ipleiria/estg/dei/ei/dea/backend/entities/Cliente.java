package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity

@NamedQueries({
        @NamedQuery(
                name = "getAllClientes",
                query = "SELECT c FROM Cliente c ORDER BY c.nome"
        ),
})
public class Cliente extends Utilizador{

    @Column(name="morada")
    private String morada;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cliente")
    private List<Encomenda> encomendas = new ArrayList<>();

    public Cliente(String username, String password, String email, String nome, String morada) {
        super(username, password, email, nome);
        this.morada = morada;

        if(encomendas == null){
            encomendas = new ArrayList<>();
        }
    }

    public Cliente(){

    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public Encomenda getEncomenda(int id) {

        for (Encomenda encomenda : this.encomendas) {
            if (encomenda.getId() == id) {
                return encomenda;
            }
        }

        return null;
    }

    public List<Encomenda> getEncomendas(){
        return  encomendas;
    }

    public void setEncomendas(List<Encomenda> encomendas) {
        this.encomendas = encomendas;
    }

    public void addEncomenda(Encomenda encomenda){
        this.encomendas.add(encomenda);
    }
}
