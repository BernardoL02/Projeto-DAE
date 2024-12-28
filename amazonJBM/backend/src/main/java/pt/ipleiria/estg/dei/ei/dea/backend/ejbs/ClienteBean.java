package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.*;
import pt.ipleiria.estg.dei.ei.dea.backend.security.Hasher;

import java.util.*;

@Stateless
public class ClienteBean {

    @PersistenceContext
    private EntityManager em;

    public void create(String username, String password, String email, String nome, String morada ){
        var cliente = new Cliente(username, Hasher.hash(password), email, nome, morada);
        em.persist(cliente);
    }

    public List<Cliente> findAll() {
        List<Cliente> clientes = em.createNamedQuery("getAllClientes", Cliente.class).getResultList();
        return clientes;
    }

    public Cliente find(String username){

        var cliente = em.find(Cliente.class, username);

        return cliente;
    }
}
