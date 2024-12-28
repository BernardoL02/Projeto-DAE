package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "tipo_caixa", nullable = false)
    private Tipo_Embalagem tipo_caixa;

    public Categoria(int id,String nome, Tipo_Embalagem tipo_caixa) {
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

    public Tipo_Embalagem getTipo_caixa() {
        return tipo_caixa;
    }

    public void setTipo_caixa(Tipo_Embalagem tipo_caixa) {
        this.tipo_caixa = tipo_caixa;
    }
}
