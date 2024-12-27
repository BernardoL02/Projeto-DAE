package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ResClienteDTO implements Serializable {

    private String username;

    public ResClienteDTO(String username) {
        this.username = username;
    }

    public ResClienteDTO(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static ResClienteDTO from(Cliente cliente) {
        return new ResClienteDTO(
                cliente.getUsername()
        );
    }

    public static List<ResClienteDTO> from(List<Cliente> clientes) {
        return clientes.stream().map(ResClienteDTO::from).collect(Collectors.toList());
    }
}
