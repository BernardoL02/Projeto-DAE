package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EmbalagemCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.VolumeCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @EJB
    private EmbalagemBean embalagemBean;

    @EJB
    private TipoEmbalagemBean tipoEmbalagemBean;

    @PostConstruct
    public void populateDB() {

        // Clientes
        clienteBean.create("Bernardo", "123", "bernas@gmail.com", "Bernardo1", "Leiria");
        clienteBean.create("Tendeiro", "123", "tendeiro@gmail.com", "ten", "Leiria");
        clienteBean.create("Sousa", "123", "sousa@gmail.com", "SousaX", "Porto");
        clienteBean.create("Ferreira", "123", "ferreira@gmail.com", "Fer", "Lisboa");
        clienteBean.create("Carvalho", "123", "carvalho@gmail.com", "Carva", "Coimbra");
        // Logistas
        logistaBean.create("Miguel", "123", "miguel@gmail.com", "Smigueli");
        logistaBean.create("Jose", "123", "jose@gmail.com", "JoseD");
        // Gestores
        gestorBean.create("Delgado", "123", "delgado@gmail.com", "José");

        tipoSensoresBean.create(1, "Temperatura");
        tipoSensoresBean.create(2, "Aceleração");
        tipoSensoresBean.create(3, "Pressão Atmosférica");
        tipoSensoresBean.create(4, "GPS");
        
        tipoEmbalagemBean.create(1, "Isotérmica", Arrays.asList(tipoSensoresBean.find(1)));
        tipoEmbalagemBean.create(2, "Original", Arrays.asList(tipoSensoresBean.find(4),tipoSensoresBean.find(2)));
        tipoEmbalagemBean.create(3, "Metalica", Arrays.asList(tipoSensoresBean.find(2), tipoSensoresBean.find(3),tipoSensoresBean.find(4)));
        tipoEmbalagemBean.create(4, "Cartao", Arrays.asList(tipoSensoresBean.find(4)));
        tipoEmbalagemBean.create(5, "E_TA", Arrays.asList(tipoSensoresBean.find(1),tipoSensoresBean.find(2)));
        tipoEmbalagemBean.create(6, "E_TG", Arrays.asList(tipoSensoresBean.find(1),tipoSensoresBean.find(4)));
        tipoEmbalagemBean.create(7, "E_TPG", Arrays.asList(tipoSensoresBean.find(1),tipoSensoresBean.find(3),tipoSensoresBean.find(4)));
        tipoEmbalagemBean.create(8, "E_TAP", Arrays.asList(tipoSensoresBean.find(1),tipoSensoresBean.find(2),tipoSensoresBean.find(3)));
        tipoEmbalagemBean.create(9, "E_AG", Arrays.asList(tipoSensoresBean.find(2),tipoSensoresBean.find(4)));
        tipoEmbalagemBean.create(10, "E_PG", Arrays.asList(tipoSensoresBean.find(3),tipoSensoresBean.find(4)));
        tipoEmbalagemBean.create(11, "E_AP", Arrays.asList(tipoSensoresBean.find(2),tipoSensoresBean.find(3)));
        tipoEmbalagemBean.create(12, "E_TAPG", Arrays.asList(tipoSensoresBean.find(1),tipoSensoresBean.find(2),tipoSensoresBean.find(3),tipoSensoresBean.find(4)));

        // Categorias de Produtos
        categoriaBean.create(1, "Alimentos");
        categoriaBean.create(2, "Tv e Som");
        categoriaBean.create(3, "Ferramentas");
        categoriaBean.create(4, "Bebidas");
        categoriaBean.create(5, "Eletrodomésticos");
        categoriaBean.create(6, "Vestuário");
        categoriaBean.create(7, "Educação");
        categoriaBean.create(9, "Esportivo");
        categoriaBean.create(10, "Casa");
        categoriaBean.create(11, "Beleza");
        categoriaBean.create(12, "Ferramentas Elétricas");


        // Produtos
        // Alimentos
        produtoBean.create(1, "Maçã", 1);
        produtoBean.create(2, "Pão Integral", 1);
        produtoBean.create(3, "Gelados", 1);

        // Tv e Som
        produtoBean.create(4, "Televisão LED 40\"", 2);
        produtoBean.create(5, "Sistema de Som", 2);
        produtoBean.create(6, "Fones", 2);

        // Ferramentas
        produtoBean.create(7, "Martelo", 3);
        produtoBean.create(8, "Chave de Fenda", 3);
        produtoBean.create(9, "Alicate", 3);

        // Bebidas
        produtoBean.create(10, "Coca Cola", 4);
        produtoBean.create(11, "Água Mineral", 4);
        produtoBean.create(12, "Sumo de Laranja", 4);

        // Eletrodomésticos
        produtoBean.create(13, "Frigorifico", 5);
        produtoBean.create(14, "Micro-ondas", 5);
        produtoBean.create(15, "Aspirador de Pó", 5);

        // Vestuário
        produtoBean.create(16, "Polo", 6);
        produtoBean.create(17, "Calça Jeans", 6);
        produtoBean.create(18, "Casaco de Lã", 6);

        // Educação
        produtoBean.create(19, "Livro de Matemática", 7);
        produtoBean.create(20, "Dicionário", 7);
        produtoBean.create(21, "Atlas Geográfico", 7);

        // Esportivo
        produtoBean.create(25, "Bola de Futebol", 9);
        produtoBean.create(26, "Raquete de Tênis", 9);
        produtoBean.create(27, "Luvas de Boxe", 9);

        // Casa
        produtoBean.create(28, "Sofá de Couro", 10);
        produtoBean.create(29, "Mesa de Jantar", 10);
        produtoBean.create(30, "Cadeira de Madeira", 10);

        // Beleza
        produtoBean.create(31, "Perfume", 11);
        produtoBean.create(32, "Creme Hidratante", 11);
        produtoBean.create(33, "Shampoo", 11);

        // Ferramentas Elétricas
        produtoBean.create(34, "Berbequins", 12);
        produtoBean.create(35, "Aparafusadora", 12);
        produtoBean.create(36, "Caixote Do Lixo", 12);


        ///////////////////////Criar Encomendas e Volumes
        ///////// Criar Encomenda para Bernardo
            List<VolumeCreateEncomendaDTO> volumesBernardo1 = new ArrayList<>();
            List<EmbalagemCreateEncomendaDTO> embalagensBernardo1 = new ArrayList<>();
            List<EmbalagemCreateEncomendaDTO> embalagensBernardo2 = new ArrayList<>();
            // Volume 1
                    VolumeCreateEncomendaDTO volume1Bernardo1 = new VolumeCreateEncomendaDTO();
                    embalagensBernardo1.add(new EmbalagemCreateEncomendaDTO(12,new ProdutoCreateEncomendaDTO(5),2));
                    embalagensBernardo1.add(new EmbalagemCreateEncomendaDTO(1,new ProdutoCreateEncomendaDTO(3),10));
                    volume1Bernardo1.setEmbalagens(embalagensBernardo1);
                    volumesBernardo1.add(volume1Bernardo1);
            // Volume 2
                    VolumeCreateEncomendaDTO volume2Bernardo1 = new VolumeCreateEncomendaDTO();
                    embalagensBernardo2.add(new EmbalagemCreateEncomendaDTO(10,new ProdutoCreateEncomendaDTO(4),2));
                    embalagensBernardo2.add(new EmbalagemCreateEncomendaDTO(2,new ProdutoCreateEncomendaDTO(10),15));
                    volume2Bernardo1.setEmbalagens(embalagensBernardo2);
                    volumesBernardo1.add(volume2Bernardo1);
            // Encomenda
                    encomendaBean.create("Bernardo", volumesBernardo1, LocalDateTime.of(2024, 10, 31, 10, 0));
                    Cliente bernardo = clienteBean.find("Bernardo");
                    encomendaBean.mudarEstadoEncomenda(1, "PorEntregar", bernardo);


        // Tipos de Sensores
        tipoSensoresBean.create(1, "Temperatura");
        tipoSensoresBean.create(2, "Aceleração");
        tipoSensoresBean.create(3, "Pressão Atmosférica");
        tipoSensoresBean.create(4, "GPS");

        // Sensores
        //Encomenda 1
        sensorBean.create("29.0", 1, 100, 30, 10, 1);
        sensorBean.create("39.7344200469475, -8.821063143811228", 4, 100, 3);
        sensorBean.create("39.73965392397057, -8.818502730615972", 4, 100, 2);
        sensorBean.create("1000", 3, 80, 1015, 980, 2);
        //Encomenda 2
        sensorBean.create("39.73513810246074, -8.799701988029124", 4, 5, 3);
        sensorBean.create("1000", 3, 100, 1015, 980,3);
        sensorBean.create("39.74986926478417, -8.808952733780515", 4, 88, 4);
        sensorBean.create("25", 2, 88, 30, 5, 4);
        //Encomenda 3
        /*sensorBean.create("21.0", 1, 99, 35, 10, 5);
        sensorBean.create("1000", 3, 80, 1015, 980, 5);
        sensorBean.create("39.75604230095242, -9.03173385047379", 4, 100, 5);
        sensorBean.create("39.60047246334607, -9.073144176554514", 4, 100, 6);
        sensorBean.create("20", 2, 100,30, 5, 6);
        //Encomenda 6
        sensorBean.create("25.0", 1, 90, 30, 15, 11);
        sensorBean.create("39.362060990104126, -9.374644032197038", 4, 100, 11);

*/
        // Alertas
        // Alerta para Temperatura
        alertaBean.create("Valor acima do limite máximo (35) para o sensor Temperatura", 1, "35", 99,1);
        alertaBean.create("Valor abaixo do limite mínimo (15) para o sensor Temperatura", 1, "10",70, 2);

        // Alerta para Aceleração
        alertaBean.create("Valor acima do limite máximo (30) para o sensor Aceleração", 6, "32",90, 1);
        alertaBean.create("Valor abaixo do limite mínimo (5) para o sensor Aceleração", 6, "3",11, 2);
        alertaBean.create("Valor abaixo do limite mínimo (5) para o sensor Aceleração", 5, "3",60, 1);

        // Alerta para Pressão Atmosférica
        alertaBean.create("Valor acima do limite máximo (1015) para o sensor Pressão Atmosférica", 4, "1016",30, 2);
        alertaBean.create("Valor abaixo do limite mínimo (980) para o sensor Pressão Atmosférica", 4, "975",50, 1);

    }

}
