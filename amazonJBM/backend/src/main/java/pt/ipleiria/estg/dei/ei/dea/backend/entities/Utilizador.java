package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "utilizadores")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Utilizador {
    @Id

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="email")
    @Email
    private String email;

    @Column(name="nome")
    private String nome;


    public Utilizador(String username, String password, String email, String nome) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.nome = nome;
    }

    public Utilizador(){

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

}
