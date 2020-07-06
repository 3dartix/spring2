package ru.geekbrains.repo;

import org.springframework.stereotype.Repository;
import ru.geekbrains.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(String name);
}
