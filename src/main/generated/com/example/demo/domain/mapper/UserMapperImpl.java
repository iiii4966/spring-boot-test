package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.CreateUserRequest;
import com.example.demo.domain.dto.UserView;
import com.example.demo.domain.model.Account;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-02-26T21:31:42+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 15.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public CreateUserRequest ToCreateUserRequest(Account account) {
        if ( account == null ) {
            return null;
        }

        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername( account.getUsername() );
        createUserRequest.setPassword( account.getPassword() );

        return createUserRequest;
    }

    @Override
    public UserView toUserView(Account account) {
        if ( account == null ) {
            return null;
        }

        UserView userView = new UserView();

        userView.setId( account.getId() );
        userView.setUsername( account.getUsername() );

        return userView;
    }
}
