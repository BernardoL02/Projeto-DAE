package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "tipo_embalagens"
)
public class Tipo_Embalagem extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;

    @ManyToMany
    @JoinTable(
            name = "embalagem_tipoSensor",
            joinColumns = @JoinColumn(
                    name = "embalagem_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tipoSensor_id",
                    referencedColumnName = "id"
            )
    )
    private List<Tipo_Sensores> tipoSensores = new ArrayList<>();


    public Tipo_Embalagem(String nome) {
        this.nome = nome;
        if(tipoSensores == null) {
            tipoSensores = new ArrayList<>();
        }
    }

    public Tipo_Embalagem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Tipo_Sensores> getTipoSensores() {
        return tipoSensores;
    }

    public void setTipoSensores(List<Tipo_Sensores> sensores) {
        this.tipoSensores = sensores;
    }

    public void addTipoSensor(Tipo_Sensores tipoSensor) {
        tipoSensores.add(tipoSensor);

    }
}
