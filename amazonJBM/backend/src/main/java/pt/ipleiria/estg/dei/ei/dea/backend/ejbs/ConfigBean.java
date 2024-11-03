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
        categoriaBean.create(2, "Tv e Som", "Original");
        categoriaBean.create(3, "Ferramentas", "Reforçada");
        categoriaBean.create(4, "Bebidas", "Isotérmica");
        categoriaBean.create(5, "Eletrodomésticos", "Reforçada");
        categoriaBean.create(6, "Vestuário", "Original");
        categoriaBean.create(7, "Educação", "Original");
        categoriaBean.create(9, "Esportivo", "Reforçada");
        categoriaBean.create(10, "Casa", "Reforçada");
        categoriaBean.create(11, "Beleza", "Original");
        categoriaBean.create(12, "Ferramentas Elétricas", "Reforçada");


        // Produtos

        // Alimentos
        produtoBean.create(1, "Maçã", 1, 5);
        produtoBean.create(2, "Pão Integral", 1, 10);
        produtoBean.create(3, "Gelados", 1, 15);

        // Tv e Som
        produtoBean.create(4, "Televisão LED 40\"", 2, 2);
        produtoBean.create(5, "Sistema de Som", 2, 3);
        produtoBean.create(6, "Fones", 2, 20);

        // Ferramentas
        produtoBean.create(7, "Martelo", 3, 25);
        produtoBean.create(8, "Chave de Fenda", 3, 30);
        produtoBean.create(9, "Alicate", 3, 15);

        // Bebidas
        produtoBean.create(10, "Coca Cola", 4, 20);
        produtoBean.create(11, "Água Mineral", 4, 30);
        produtoBean.create(12, "Sumo de Laranja", 4, 25);

        // Eletrodomésticos
        produtoBean.create(13, "Frigorifico", 5, 1);
        produtoBean.create(14, "Micro-ondas", 5, 5);
        produtoBean.create(15, "Aspirador de Pó", 5, 10);

        // Vestuário
        produtoBean.create(16, "Polo", 6, 20);
        produtoBean.create(17, "Calça Jeans", 6, 15);
        produtoBean.create(18, "Casaco de Lã", 6, 5);

        // Educação
        produtoBean.create(19, "Livro de Matemática", 7, 10);
        produtoBean.create(20, "Dicionário", 7, 5);
        produtoBean.create(21, "Atlas Geográfico", 7, 8);

        // Esportivo
        produtoBean.create(25, "Bola de Futebol", 9, 10);
        produtoBean.create(26, "Raquete de Tênis", 9, 5);
        produtoBean.create(27, "Luvas de Boxe", 9, 8);

        // Casa
        produtoBean.create(28, "Sofá de Couro", 10, 1);
        produtoBean.create(29, "Mesa de Jantar", 10, 1);
        produtoBean.create(30, "Cadeira de Madeira", 10, 6);

        // Beleza
        produtoBean.create(31, "Perfume", 11, 15);
        produtoBean.create(32, "Creme Hidratante", 11, 20);
        produtoBean.create(33, "Shampoo", 11, 30);

        // Ferramentas Elétricas
        produtoBean.create(34, "Berbequins", 12, 8);
        produtoBean.create(35, "Aparafusadora", 12, 10);
        produtoBean.create(36, "Caixote Do Lixo", 12, 4);



        // Encomendas e Volumes
        encomendaBean.create("Bernardo", "PorEntregar", LocalDateTime.of(2024, 10, 29, 15, 30), LocalDateTime.of(2024, 10, 29, 15, 30));
        volumeBean.create(3, 5, 1);
        volumeBean.create(31, 1, 1);

        encomendaBean.create("Tendeiro", "Entregue", LocalDateTime.of(2024, 10, 29, 15, 30), LocalDateTime.of(2024, 10, 29, 15, 30));
        volumeBean.create(35, 1, 2);
        volumeBean.create(29, 1, 2);

        encomendaBean.create("Sousa", "PorEntregar", LocalDateTime.of(2024, 11, 2, 12, 0), LocalDateTime.of(2024, 11, 2, 12, 30));
        volumeBean.create(25, 2, 3);
        volumeBean.create(13, 1, 3);

        encomendaBean.create("Bernardo", "EmProcessamento", LocalDateTime.of(2024, 10, 29, 15, 30), LocalDateTime.of(2024, 10, 29, 15, 30));
        volumeBean.create(3, 5, 4);
        volumeBean.create(6, 1, 4);

        encomendaBean.create("Ferreira", "EmProcessamento", LocalDateTime.of(2023, 10, 29, 15, 30), LocalDateTime.of(2023, 10, 29, 15, 30));
        volumeBean.create(8, 2, 5);
        volumeBean.create(16, 1, 5);

        encomendaBean.create("Ferreira", "Cancelada", LocalDateTime.of(2023, 10, 29, 15, 30), LocalDateTime.of(2023, 10, 29, 15, 30));
        volumeBean.create(12, 1, 6);


        // Tipos de Sensores
        tipoSensoresBean.create(1, "Temperatura");
        tipoSensoresBean.create(2, "Aceleração");
        tipoSensoresBean.create(3, "Pressão Atmosférica");
        tipoSensoresBean.create(4, "GPS");

        // Sensores
        //Encomenda 1
        sensorBean.create("29.0", 1, "ativo", 100, 30, 10, 1);
        sensorBean.create("39.7344200469475, -8.821063143811228", 4, "ativo", 100, 1);
        sensorBean.create("39.73965392397057, -8.818502730615972", 4, "ativo", 100, 2);
        sensorBean.create("1000", 3, "ativo", 80, 1015, 980, 2);
        //Encomenda 2
        sensorBean.create("39.73513810246074, -8.799701988029124", 4, "inativo", 5, 3);
        sensorBean.create("1000", 3, "inativo", 100, 1015, 980,3);
        sensorBean.create("39.74986926478417, -8.808952733780515", 4, "inativo", 88, 4);
        sensorBean.create("25", 2, "inativo", 88, 30, 5, 4);
        //Encomenda 3
        sensorBean.create("21.0", 1, "ativo", 99, 35, 10, 5);
        sensorBean.create("1000", 3, "ativo", 80, 1015, 980, 5);
        sensorBean.create("39.75604230095242, -9.03173385047379", 4, "ativo", 100, 5);
        sensorBean.create("39.60047246334607, -9.073144176554514", 4, "ativo", 100, 6);
        sensorBean.create("20", 2, "ativo", 100,30, 5, 6);
        //Encomenda 6
        sensorBean.create("25.0", 1, "inativo", 90, 30, 15, 11);
        sensorBean.create("39.362060990104126, -9.374644032197038", 4, "inativo", 100, 11);


        // Alertas
        // Alerta para Temperatura
        alertaBean.create("Valor acima do limite máximo (35) para o sensor Temperatura", 1, "35", 1);
        alertaBean.create("Valor abaixo do limite mínimo (15) para o sensor Temperatura", 1, "10", 1);

        // Alerta para Aceleração
        alertaBean.create("Valor acima do limite máximo (30) para o sensor Aceleração", 6, "32", 4);
        alertaBean.create("Valor abaixo do limite mínimo (5) para o sensor Aceleração", 6, "3", 4);
        alertaBean.create("Valor abaixo do limite mínimo (5) para o sensor Aceleração", 10, "3", 6);

        // Alerta para Pressão Atmosférica
        alertaBean.create("Valor acima do limite máximo (1015) para o sensor Pressão Atmosférica", 4, "1016", 3);
        alertaBean.create("Valor abaixo do limite mínimo (980) para o sensor Pressão Atmosférica", 4, "975", 3);


    }

}
