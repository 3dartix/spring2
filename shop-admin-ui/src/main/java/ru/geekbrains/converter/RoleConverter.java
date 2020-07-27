package ru.geekbrains.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.entity.Role;
import ru.geekbrains.repo.RoleRepository;

@Component
public class RoleConverter implements Converter<String, Role> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(String s) {
        return roleRepository.findRoleByName(s).orElse(null);
    }
}
