package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ResProdutoDTO implements Serializable {

    private int id;
    private String nome;

    public ResProdutoDTO(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ResProdutoDTO(){

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

    public static ResProdutoDTO from(Produto produto) {
        return new ResProdutoDTO(
                produto.getId(),
                produto.getNome()
        );
    }

    public static List<ResProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ResProdutoDTO::from).collect(Collectors.toList());
    }
}
