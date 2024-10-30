package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Encomenda;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDTO implements Serializable {

    private String username;

    private String password;

    private String email;

    private String nome;

    private String morada;

    private List<EncomendasDTO> encomendas = new ArrayList<>();

    public ClienteDTO(String username, String password, String email, String nome, String morada) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
        this.morada = morada;

        if(encomendas == null) {
            encomendas = new ArrayList<>();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public List<EncomendasDTO> getEncomendas() {
        return encomendas;
    }

    public static ClienteDTO from(Cliente cliente) {
        return new ClienteDTO(
                cliente.getUsername(),
                cliente.getPassword(),
                cliente.getEmail(),
                cliente.getNome(),
                cliente.getMorada()
        );
    }

    // converts an entire list of entities into a list of DTOs
    public static List<ClienteDTO> from(List<Cliente> students) {
        return students.stream().map(ClienteDTO::from).collect(Collectors.toList());
    }

    public void setEncomendas(List<EncomendasDTO> encomendas) {
        this.encomendas = encomendas;
    }
}
