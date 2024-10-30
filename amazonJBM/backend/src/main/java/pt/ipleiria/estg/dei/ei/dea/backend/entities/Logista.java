package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity

@NamedQueries({
        @NamedQuery(
                name = "getAllLogistas",
                query = "SELECT l FROM Logista l"
        ),
})
public class Logista extends Utilizador{

    public Logista(String username, String password, String email, String nome) {
        super(username, password, email, nome);
    }

    public Logista(){

    }
}
