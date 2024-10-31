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

    public void create(int id, String mensagem, int id_sensor, String valor, String username, int id_encomenda){

        Utilizador utilizador = em.find(Utilizador.class, username);
        Sensor sensor = em.find(Sensor.class, id_sensor);
        Encomenda encomenda = em.find(Encomenda.class, id_encomenda);

        var alerta = new Alerta(id, mensagem, sensor, valor, utilizador, encomenda);
        em.persist(alerta);
    }

}
