package com.azkafadhli.belajarspringdata.services;

import com.azkafadhli.belajarspringdata.dtos.requests.GetUsersDTO;
import com.azkafadhli.belajarspringdata.dtos.requests.RegisterUserDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserDetailsDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserListDTO;

import java.util.UUID;

public interface IUserService {

    UserListDTO getUsers(GetUsersDTO getUsersDTO);
    void addUser(RegisterUserDTO userRequest);
    UserDetailsDTO getUserById(UUID id);

}
