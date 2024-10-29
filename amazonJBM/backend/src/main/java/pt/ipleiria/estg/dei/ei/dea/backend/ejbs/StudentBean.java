package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Student;

@Stateless
public class StudentBean {
    @PersistenceContext
    private EntityManager entityManager;
    public void create(String username, String nome) {
        var student = new Student(username,nome);
        entityManager.persist(student);
    }
}
