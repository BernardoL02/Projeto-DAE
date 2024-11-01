package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Startup
@Singleton
public class ConfigBean {

    @EJB
    private EncomendaBean encomendaBean;

    @EJB
    private ClienteBean clienteBean;

    @EJB
    private ProdutoBean produtoBean;

    @EJB
    private CategoriaBean categoriaBean;

    @EJB
    private TipoSensoresBean tipoSensoresBean;

    @EJB
    private SensorBean sensorBean;

    @EJB
    private VolumeBean volumeBean;

    @EJB
    private AlertaBean alertaBean;

    @EJB
    private GestorBean gestorBean;

    @EJB
    private LogistaBean logistaBean;

    @PostConstruct
    public void populateDB() {

        //Users
        clienteBean.create("Bernardo", "12#45", "bernas@gmail.com", "Bernardo1", "Leira");
        logistaBean.create("Miguel", "123", "miguel@gmail.com", "Smigueli");
        gestorBean.create("Delgado", "123", "delgado@gmail.com", "José");
        clienteBean.create("Tendeiro", "12#45", "tendeiro@gmail.com", "ten", "Leira");

        //Categorias dos Produtos
        categoriaBean.create(1, "Alimentos", "Isotérmica");
        categoriaBean.create(2, "Tv e Som", "Isotérmica");

        //Produtos
        produtoBean.create(1,"Gelados", 1, 10);
        produtoBean.create(2,"Banana", 1, 10);
        produtoBean.create(3,"TV", 2, 10);

        //Encomendas
        encomendaBean.create(1,"Bernardo","PorEntregar",
                LocalDateTime.of(2024, 10, 29, 15, 30),
                LocalDateTime.of(2024, 10, 29, 15, 30)
                );
        encomendaBean.create(2,"Bernardo","EmProcessamento",
                LocalDateTime.of(2024, 10, 29, 15, 30),
                LocalDateTime.of(2024, 10, 29, 15, 30)
                );
        encomendaBean.create(3,"Tendeiro","Entregue",
                LocalDateTime.of(2024, 10, 29, 15, 30),
                LocalDateTime.of(2024, 10, 29, 15, 30)
        );
        encomendaBean.create(4,"Tendeiro","EmProcessamento",
                LocalDateTime.of(2023, 10, 29, 15, 30),
                LocalDateTime.of(2023, 10, 29, 15, 30)
        );

        //Volumes
        volumeBean.create(1, 1, 10, 1);
        volumeBean.create(2, 2, 1, 1);
        volumeBean.create(3, 2, 1, 2);
        volumeBean.create(4, 2, 1, 3);

        //Tipos de Sensores
        tipoSensoresBean.create(1, "Temperatura");
        tipoSensoresBean.create(2, "Acelaracao");
        tipoSensoresBean.create(3, "Pressao Atmosferia");
        tipoSensoresBean.create(4, "GPS");

        //Sensores
        sensorBean.create(1, "29.0",1,"ativo", 100,40,10,1);
        sensorBean.create(2, "40.7123, -74.12312",4,"ativo", 100,1);
        sensorBean.create(3, "28.0",3,"ativo", 100,10,5,3);
        sensorBean.create(4, "21.0",1,"ativo", 100,10,5,1);
        sensorBean.create(5, "21.0",2,"ativo", 99,10,5,4);

        //Alertas
        //alertaBean.create(1,"A comida está a descongelarrr",1, "Bernardo", 1);
        //alertaBean.create(2,"A comida está a descongelarrr",1, "Bernardo", 1);
        //alertaBean.create(4,"O banana tem muita acelaracao",5, "Tendeiro", 3);
    }
}
