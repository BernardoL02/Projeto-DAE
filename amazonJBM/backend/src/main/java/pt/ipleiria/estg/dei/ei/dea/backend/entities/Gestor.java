package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.Entity;

@Entity
public class Gestor extends Utilizador{


    public Gestor(String username, String password, String email, String nome) {
        super(username, password, email, nome);
    }

    public Gestor(){

    }
}
