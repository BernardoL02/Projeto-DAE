package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;
import org.glassfish.jaxb.core.v2.TODO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "encomendas"
)

@NamedQueries({
        @NamedQuery(
                name = "getAllEncomendas",
                query = "SELECT e FROM Encomenda e ORDER BY e.id" // JPQL
        )

})


public class Encomenda extends Versionable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private String estado;

    private LocalDateTime data_expedicao;

    private LocalDateTime data_entrega;

    @OneToMany(mappedBy = "encomenda", fetch = FetchType.EAGER)
    private List<Volume> volumes = new ArrayList<>();

    public Encomenda(Cliente cliente, LocalDateTime data_expedicao) {
        this.cliente = cliente;
        this.estado = "EmProcessamento";
        this.data_expedicao = data_expedicao;

        if(volumes == null) {
            volumes = new ArrayList<>();
        }
    }

    public Encomenda(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getData_expedicao() {
        return data_expedicao;
    }

    public void setData_expedicao(LocalDateTime data_expedicao) {
        this.data_expedicao = data_expedicao;
    }

    public LocalDateTime getData_entrega() {
        return data_entrega;
    }

    public void setData_entrega(LocalDateTime data_entrega) {
        this.data_entrega = data_entrega;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public void addVolume(Volume volume) {
        volumes.add(volume);
    }

    public List<Produto> getProdutos(){
        List<Produto> produtos = new ArrayList<>();

        for (Volume volume: volumes) {
            for(Embalagem embalagem : volume.getEmbalagens()){
                produtos.add(embalagem.getProduto());
            }
        }
        return produtos;
    }

}
