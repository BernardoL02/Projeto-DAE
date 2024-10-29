package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;


import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Alerta;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Sensor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;

@Stateless
public class AlertaBean {
    @PersistenceContext
    private EntityManager em;

    public void create(String mensagem, int id_sensor, int id_utilizador, int id_encomenda){
        var alerta = new Alerta(mensagem, sensor, utilizador, encomenda);

        em.persist(alerta);
    }

}
