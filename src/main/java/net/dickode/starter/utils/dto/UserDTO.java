package net.dickode.starter.utils;

import lombok.Data;
import net.dickode.starter.dao.entity.Role;
import net.dickode.starter.dao.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class UserDTO {

    @Id
    private Long id;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @NotNull(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @NotNull(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "Le prénom est obligatoire")
    @NotNull(message = "Le prénom est obligatoire")
    private String firstname;

    @NotBlank(message = "Le nom est obligatoire")
    @NotNull(message = "Le nom est obligatoire")
    private String lastname;

    private boolean enabled = true;

    private Collection<String> roles = new ArrayList<>();

    public User toEntity() {
        User user = new User();

        user.setUsername(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEnabled(enabled);

        return user;
    }
}
