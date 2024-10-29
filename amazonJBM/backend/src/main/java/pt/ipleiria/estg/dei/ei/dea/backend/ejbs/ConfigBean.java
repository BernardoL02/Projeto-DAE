package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.time.LocalDateTime;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private EncomendaBean encomendaBean;

    @EJB ClienteBean clienteBean;

    @PostConstruct
    public void populateDB() {
        System.out.println("Hello Java EE!");

        clienteBean.create("Bernardo", "12#45", "bernas@gmail.com", "Bernardo1", "Leira");
        encomendaBean.create(1,"Bernardo","Por Entregar",LocalDateTime.of(2024, 10, 29, 15, 30),LocalDateTime.of(2024, 10, 29, 15, 30));
    }
}
