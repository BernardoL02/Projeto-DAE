package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;

import java.util.NoSuchElementException;

@Stateless
public class ClienteBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String email, String nome, String morada ){
        var cliente = new Cliente(username, password, email, nome, morada);
        em.persist(cliente);
    }

    public Cliente find(String username){

        var cliente = em.find(Cliente.class, username);

        if(cliente == null){
            throw new NoSuchElementException();
        }

        return cliente;
    }
}
