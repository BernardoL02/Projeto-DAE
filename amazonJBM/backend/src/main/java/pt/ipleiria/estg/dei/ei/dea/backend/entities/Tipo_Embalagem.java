package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "tipo_embalagem"
)
@NamedQueries({
        @NamedQuery(
                name = "getTipoEmbalagem",
                query = "SELECT t FROM Tipo_Embalagem t "
        ),
})
public class Tipo_Embalagem {
        @Id
        private int id;

        private String tipo;

        @ManyToMany
        @JoinTable(
                name = "tipo_embalagem_tipo_sensores",
                joinColumns = @JoinColumn(name = "id_TipoEmbalabem", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "id_TipoSensor", referencedColumnName = "id")
        )
        private List<Tipo_Sensores> tipoSensores = new ArrayList<>();

        public Tipo_Embalagem(int id, String tipo, List<Tipo_Sensores> tipoSensores) {
                this.id = id;
                this.tipo = tipo;
                this.tipoSensores = tipoSensores;
        }

        public Tipo_Embalagem() {}

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getTipo() {
                return tipo;
        }

        public void setTipo(String tipo) {
                this.tipo = tipo;
        }

        public List<Tipo_Sensores> getSensores() {
                return tipoSensores;
        }

        public void addSensor(Tipo_Sensores idTipo) {
                tipoSensores.add(idTipo);
        }
}
