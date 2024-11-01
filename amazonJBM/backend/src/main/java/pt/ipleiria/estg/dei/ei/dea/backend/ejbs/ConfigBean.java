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

        // Clientes
        clienteBean.create("Bernardo", "12#45", "bernas@gmail.com", "Bernardo1", "Leiria");
        clienteBean.create("Tendeiro", "12#45", "tendeiro@gmail.com", "ten", "Leiria");
        clienteBean.create("Sousa", "45678", "sousa@gmail.com", "SousaX", "Porto");
        clienteBean.create("Ferreira", "78#90", "ferreira@gmail.com", "Fer", "Lisboa");
        clienteBean.create("Carvalho", "abc123", "carvalho@gmail.com", "Carva", "Coimbra");
        // Logistas
        logistaBean.create("Miguel", "123", "miguel@gmail.com", "Smigueli");
        // Gestores
        gestorBean.create("Delgado", "123", "delgado@gmail.com", "José");

        // Categorias de Produtos
        categoriaBean.create(1, "Alimentos", "Isotérmica");
        categoriaBean.create(2, "Tv e Som", "Eletrónica");
        categoriaBean.create(3, "Ferramentas", "Industrial");
        categoriaBean.create(4, "Bebidas", "Isotérmica");
        categoriaBean.create(5, "Eletrodomésticos", "Eletrónica");

        // Produtos
        produtoBean.create(1, "Gelados", 1, 10);
        produtoBean.create(2, "Banana", 1, 5);
        produtoBean.create(3, "TV", 2, 15);
        produtoBean.create(4, "Refrigerante", 4, 20);
        produtoBean.create(5, "Martelo", 3, 25);
        produtoBean.create(6, "Computador", 2, 8);
        produtoBean.create(7, "Suco de Laranja", 4, 30);
        produtoBean.create(8, "Ar Condicionado", 5, 12);

        // Encomendas
        encomendaBean.create(1, "Bernardo", "PorEntregar", LocalDateTime.of(2024, 10, 29, 15, 30), LocalDateTime.of(2024, 10, 29, 15, 30));
        encomendaBean.create(2, "Bernardo", "EmProcessamento", LocalDateTime.of(2024, 10, 29, 15, 30), LocalDateTime.of(2024, 10, 29, 15, 30));
        encomendaBean.create(3, "Tendeiro", "Entregue", LocalDateTime.of(2024, 10, 29, 15, 30), LocalDateTime.of(2024, 10, 29, 15, 30));
        encomendaBean.create(4, "Tendeiro", "EmProcessamento", LocalDateTime.of(2023, 10, 29, 15, 30), LocalDateTime.of(2023, 10, 29, 15, 30));
        encomendaBean.create(5, "Sousa", "PorEntregar", LocalDateTime.of(2024, 11, 2, 12, 0), LocalDateTime.of(2024, 11, 2, 12, 30));

        // Volumes
        volumeBean.create(1, 1, 10, 1);
        volumeBean.create(2, 2, 1, 1);
        volumeBean.create(3, 2, 1, 2);
        volumeBean.create(4, 2, 1, 3);
        volumeBean.create(5, 3, 5, 4);
        volumeBean.create(6, 4, 2, 1);
        volumeBean.create(7, 5, 4, 3);
        volumeBean.create(8, 6, 1, 4);

        // Tipos de Sensores
        tipoSensoresBean.create(1, "Temperatura");
        tipoSensoresBean.create(2, "Aceleração");
        tipoSensoresBean.create(3, "Pressão Atmosférica");
        tipoSensoresBean.create(4, "GPS");

        // Sensores
        sensorBean.create(1, "29.0", 1, "ativo", 100, 30, 10, 1);
        sensorBean.create(2, "40.7123, -74.12312", 4, "ativo", 100, 1);
        sensorBean.create(3, "28.0", 3, "ativo", 100, 50, 5, 3);
        sensorBean.create(4, "21.0", 1, "ativo", 100, 35, 5, 1);
        sensorBean.create(5, "21.0", 2, "ativo", 99, 40, 10, 4);
        sensorBean.create(6, "22.0", 2, "ativo", 80, 30, 15, 5);
        sensorBean.create(7, "25.0", 1, "ativo", 90, 30, 15, 2);
        sensorBean.create(8, "40.7123, -74.12312", 4, "ativo", 88, 1);

        // Alertas
        alertaBean.create("A comida está a descongelar", 1, "35", 1);
        alertaBean.create( "Fora do Alcance", 2, "40.7123, -74.12312", 2);
        alertaBean.create("A banana tem muita aceleração", 5, "23", 4);
        alertaBean.create( "Pressão fora do limite", 3, "1010 hPa", 3);
        alertaBean.create( "Humidade alta", 6, "60%", 5);
        alertaBean.create( "GPS desconectado", 2, "Indisponível", 2);
        alertaBean.create( "Temperatura baixa", 1, "15", 1);
        alertaBean.create( "Humidade crítica", 6, "80%", 5);
    }

}
