package pt.ipleiria.estg.dei.ei.dea.backend.dtos;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class AuthDTO implements Serializable {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public AuthDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthDTO() {
    }

    public @NotBlank String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank String username) {
        this.username = username;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
}
