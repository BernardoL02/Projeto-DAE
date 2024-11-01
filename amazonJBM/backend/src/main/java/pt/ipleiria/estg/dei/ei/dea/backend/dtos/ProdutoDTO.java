package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Categoria;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Produto;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDTO implements Serializable {

    private int id;
    private String nome;
    private String categoria;
    private int quantidade_por_volume;

    public ProdutoDTO(int id, String nome, String categoria, int quantidade_por_volume) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade_por_volume = quantidade_por_volume;
    }

    public ProdutoDTO(){

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getQuantidade_por_volume() {
        return quantidade_por_volume;
    }

    public void setQuantidade_por_volume(int quantidade_por_volume) {
        this.quantidade_por_volume = quantidade_por_volume;
    }

    public static ProdutoDTO from(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria().getNome(),
                produto.getQuantidade_por_volume()
        );
    }

    public static List<ProdutoDTO> from(List<Produto> produtos) {
        return produtos.stream().map(ProdutoDTO::from).collect(Collectors.toList());
    }
}
