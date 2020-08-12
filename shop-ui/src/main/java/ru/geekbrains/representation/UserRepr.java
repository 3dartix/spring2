package ru.geekbrains.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.entity.Role;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRepr {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым")
    private String username;

    @NotBlank(message = "Поле не может быть пустым!!!")
    private String password;

    @NotBlank(message = "Поле не может быть пустым")
    private String confirmPassword;

    @NotBlank(message = "Поле не может быть пустым")
    private String firstName;

    @NotBlank(message = "Поле не может быть пустым")
    private String lastName;

    @NotBlank(message = "Поле не может быть пустым")
    private String phone;

    @NotBlank(message = "Поле не может быть пустым")
    private String email;

//    @NotNull(message = "Выберите хотя бы одну роль")
//    @Size(min = 1, message = "Выберите хотя бы одну роль")
    private List<Role> roles;

    public String getRolesLine() {
        String result = "";
        if(roles != null) {
            for (Role role : roles) {
                result = result + ", " + role.getName().replace("ROLE_", "");
            }
        }
        result = result.replaceFirst(", ", "");
        return result;
    }

    @Override
    public String toString() {
        return "UserRepr{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
