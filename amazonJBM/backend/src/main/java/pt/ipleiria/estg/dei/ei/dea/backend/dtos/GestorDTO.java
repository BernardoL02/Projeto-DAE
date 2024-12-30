package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Cliente;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Gestor;
import pt.ipleiria.estg.dei.ei.dea.backend.entities.Logista;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/*public class GestorDTO implements Serializable {

    private String username;
    private String password;
    private String email;
    private String nome;

    public GestorDTO(String username, String password, String email, String nome) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
    }

    public GestorDTO(){}

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

    public static GestorDTO from(Gestor gestor) {
        return new GestorDTO(
                gestor.getUsername(),
                gestor.getPassword(),
                gestor.getEmail(),
                gestor.getNome()
        );
    }

    public static List<GestorDTO> from(List<Gestor> gestor) {
        return gestor.stream().map(GestorDTO::from).collect(Collectors.toList());
    }


}*/
