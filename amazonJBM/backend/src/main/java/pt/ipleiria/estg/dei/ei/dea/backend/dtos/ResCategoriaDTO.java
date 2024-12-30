package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ResCategoriaDTO implements Serializable {

    private int id;

    private String nome;

    public ResCategoriaDTO(int id,String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ResCategoriaDTO() {

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

    public static ResCategoriaDTO from(Categoria categoria) {
        return new ResCategoriaDTO(
                categoria.getId(),
                categoria.getNome()
        );
    }

    public static List<ResCategoriaDTO> from(List<Categoria> categorias) {
        return categorias.stream().map(ResCategoriaDTO::from).collect(Collectors.toList());
    }
}
