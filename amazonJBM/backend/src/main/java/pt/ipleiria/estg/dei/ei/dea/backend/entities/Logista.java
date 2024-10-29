package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.Entity;

@Entity
public class Logista extends Utilizador{

    public Logista(String username, String password, String email, String nome) {
        super(username, password, email, nome);
    }

    public Logista(){

    }
}
