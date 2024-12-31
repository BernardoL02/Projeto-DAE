package pt.ipleiria.estg.dei.ei.dea.backend.entities;


import jakarta.persistence.*;

@Entity

@NamedQueries({
        @NamedQuery(
                name = "getAllCategorias",
                query = "SELECT c FROM Categoria c"
        )
})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nome;


    public Categoria(String nome) {
        this.nome = nome;
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

}
