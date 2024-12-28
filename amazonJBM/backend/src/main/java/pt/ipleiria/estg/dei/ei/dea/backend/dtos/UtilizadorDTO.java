package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Utilizador;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class UtilizadorDTO implements Serializable {

    private String username;
    private String name;
    private String email;
    private String role;

    public UtilizadorDTO() {
    }

    public UtilizadorDTO(String username, String name, String email, String role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    public static UtilizadorDTO from(Utilizador user) {
        return new UtilizadorDTO(
                user.getUsername(),
                user.getNome(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static List<UtilizadorDTO> from(List<Utilizador> users) {
        return users.stream().map(UtilizadorDTO::from).collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
