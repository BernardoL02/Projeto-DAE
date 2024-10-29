package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity

@NamedQueries({
        @NamedQuery(
                name = "getcategoriaById",
                query = "SELECT c FROM Categoria c"
        ),
})
public class Categoria {

    @Id
    private int id;

    private String nome;

    private String tipo_caixa;

    public Categoria(int id,String nome, String tipo_caixa) {
        this.id = id;
        this.nome = nome;
        this.tipo_caixa = tipo_caixa;
    }

    public Categoria() {

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

    public String getTipo_caixa() {
        return tipo_caixa;
    }

    public void setTipo_caixa(String tipo_caixa) {
        this.tipo_caixa = tipo_caixa;
    }
}
