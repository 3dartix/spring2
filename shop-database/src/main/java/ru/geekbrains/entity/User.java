package ru.geekbrains.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым")
    @Column(length = 64)
    private String name;

    @NotBlank(message = "Поле не может быть пустым")
    @Column(length = 128)
    private String password;

    @NotNull(message = "Выберите хотя бы одну роль")
    @Size(min = 1, message = "Выберите хотя бы одну роль")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
                joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    public String getCategoriesLine() {
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
        return "User{" +
                "id=" + id;
    }
}
