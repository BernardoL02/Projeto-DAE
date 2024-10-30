package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import pt.ipleiria.estg.dei.ei.dea.backend.entities.Logista;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class LogistaDTO implements Serializable {

    private String username;
    private String password;
    private String email;
    private String nome;

    public LogistaDTO(String username, String password, String email, String nome){
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
    }

    public LogistaDTO(){

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

    public static LogistaDTO from(Logista logista) {
        return new LogistaDTO(
                logista.getUsername(),
                logista.getPassword(),
                logista.getEmail(),
                logista.getNome()
        );
    }

    public static List<LogistaDTO> from(List<Logista> logista) {

        return logista.stream().map(LogistaDTO::from).collect(Collectors.toList());
    }
}
