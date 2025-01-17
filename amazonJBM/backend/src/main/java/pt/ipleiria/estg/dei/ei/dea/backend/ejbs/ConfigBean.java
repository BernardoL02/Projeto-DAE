package pt.ipleiria.estg.dei.ei.dea.backend.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.EmbalagemCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.ProdutoCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.TipoSensorDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.dtos.VolumeCreateEncomendaDTO;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Startup
@Singleton
public class ConfigBean {

    @PersistenceContext
    private EntityManager em;

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

        ImportCSVDynamic("utilizadores", "utilizadores.csv", "dtype,username,email,nome,password,morada", "?,?,?,?,?,?");
        ImportCSVDynamic("tipo_sensores", "tiposSensor.csv", "id,tipo", "?,?");
        ImportCSVDynamic("tipo_embalagem", "tipoEmbalagem.csv", "id,tipo", "?,?");
        ImportCSVDynamic("tipo_embalagem_tipo_sensores", "tiposEmbalagemTiposSensor.csv", "id_tipoembalagem,id_tiposensor", "?,?");
        ImportCSVDynamic("categoria", "categorias.csv", "nome", "?");
        ImportCSVDynamic("produto", "produtos.csv", "id, nome, categoria_id", "?,?,?");
        ImportCSVDynamic("encomendas", "encomendas.csv", "id,version,data_entrega,data_expedicao,estado,cliente_id", "?,?,?,?,?,?");
        ImportCSVDynamic("volume", "volumes.csv", "id,entregue, encomenda_id", "?,?,?");
        ImportCSVDynamic("embalagem", "embalagens.csv", "id,quantidade,produto_id,id_tipo,id_volume", "?,?,?,?,?");
        ImportCSVDynamic("sensor", "sensores.csv", "id,bateria,estado,time_stamp,val_max,val_min,valor,id_embalagem,id_tipo", "?,?,?,?,?,?,?,?,?");
        ImportCSVDynamic("alerta", "alertas.csv", "bateria,mensagem,time_stamp,valor,id_sensor,id_volume", "?,?,?,?,?,?");
    }

    private void ImportCSVDynamic(String tableName, String fileName, String colunas, String placeholders) {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dados/" + fileName);
        String jdbcURL = "jdbc:postgresql://db:5432/backend";
        String username = "postgres";
        String password = "dbsecret";

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, colunas, placeholders);

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] valuesArray = line.split(",");

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    for (int i = 0; i < valuesArray.length; i++) {
                        if (isNumeric(valuesArray[i])) {
                            statement.setInt(i + 1, Integer.parseInt(valuesArray[i]));
                        } else if (isBoolean(valuesArray[i])) {
                            statement.setBoolean(i + 1, Boolean.parseBoolean(valuesArray[i]));
                        } else if (valuesArray[i].isEmpty() || "-".equals(valuesArray[i])) {
                            statement.setNull(i + 1, Types.TIMESTAMP);
                        }else if(isTimestamp(valuesArray[i])) {
                            statement.setTimestamp(i + 1, Timestamp.valueOf(valuesArray[i]));
                        } else {
                            statement.setString(i + 1, valuesArray[i]);
                        }
                    }
                    statement.executeUpdate();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isTimestamp(String str) {
        try {
            Timestamp.valueOf(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isBoolean(String str) {
        return "true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str);
    }

}
