package ru.geekbrains.representation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.geekbrains.entity.User;
import ru.geekbrains.representation.UserRepr;

import java.util.List;

@Mapper
public interface UserMapper {
    //ссылка на этот маппер
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    User toUser (UserRepr userRepr);

    UserRepr fromUser (User user);

    List<User> toUserList(List<UserRepr> userReprList);

    List<UserRepr> FromUserList(List<User> userList);
}
