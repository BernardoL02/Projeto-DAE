package pt.ipleiria.estg.dei.ei.dea.backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Student {

    @Id
    public String username;

    @NotNull
    public String nome;

    public Student(String username, String nome) {
        this.username = username;
        this.nome = nome;
    }

    public Student(){

    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
