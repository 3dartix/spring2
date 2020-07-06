package ru.geekbrains.representation;

import lombok.Data;
import ru.geekbrains.entity.Role;
import ru.geekbrains.entity.User;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class UserRepr {

    private Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private List<Role> roles;

    public UserRepr() {
    }

    public UserRepr(User user) {
        this.id = user.getId();
        this.username = user.getName();
        this.password = user.getPassword();
//        this.firstName = user.getFirstName();
//        this.lastName = user.getLastName();
//        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
