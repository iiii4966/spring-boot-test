package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.CreateUserRequest;
import com.example.demo.domain.dto.UserView;
import com.example.demo.domain.model.Account;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    CreateUserRequest ToCreateUserRequest(Account account);
    UserView toUserView(Account account);

    default Account createUserRequestToUser(CreateUserRequest dto) {
        if (dto == null) {
            return null;
        }
        return Account.createNew(dto.getUsername(), dto.getPassword());
    }
}
