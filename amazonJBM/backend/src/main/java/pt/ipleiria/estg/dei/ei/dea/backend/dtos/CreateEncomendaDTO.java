package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateEncomendaDTO implements Serializable {

    private int id;
    private String username;
    private String estado;
    private LocalDateTime data_expedicao;
    private LocalDateTime data_entrega;
    private List<ProdutoDTO> produtos = new ArrayList<>();

    public CreateEncomendaDTO(int id, String username, String estado, LocalDateTime data_expedicao, LocalDateTime data_entrega, List<ProdutoDTO> produtos) {
        this.id = id;
        this.username = username;
        this.estado = estado;
        this.data_expedicao = data_expedicao;
        this.data_entrega = data_entrega;
        this.produtos = produtos;
    }

    public CreateEncomendaDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public static CreateEncomendaDTO from(Encomenda encomenda) {
        List<ProdutoDTO> produtosDTO = encomenda.getProdutos().stream()
                .map(produto -> new ProdutoDTO(produto.getId(), produto.getNome(), produto.getCategoria().getNome(),produto.getQuantidade_por_volume()))
                .collect(Collectors.toList());

        return new CreateEncomendaDTO(
                encomenda.getId(),
                encomenda.getCliente().getUsername(),
                encomenda.getEstado(),
                encomenda.getData_expedicao(),
                encomenda.getData_entrega(),
                produtosDTO
        );
    }

    public static List<CreateEncomendaDTO> from(List<Encomenda> encomendas) {
        return encomendas.stream().map(CreateEncomendaDTO::from).collect(Collectors.toList());
    }
}
