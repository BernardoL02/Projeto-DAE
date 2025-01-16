package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CreateProdutoDTO implements Serializable {

    private int id;

    private String nome;

    private int id_categoria;

    public CreateProdutoDTO(int id, String nome, int id_categoria) {
        this.id = id;
        this.nome = nome;
        this.id_categoria = id_categoria;
    }

    public CreateProdutoDTO(){

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

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public static CreateProdutoDTO from(Produto produto) {
        return new CreateProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria().getId()
        );
    }

    public static List<CreateProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(CreateProdutoDTO::from).collect(Collectors.toList());
    }
}
