package com.azkafadhli.belajarspringdata.services;

import com.azkafadhli.belajarspringdata.dtos.responses.PaginationDTO;
import com.azkafadhli.belajarspringdata.dtos.requests.GetUsersDTO;
import com.azkafadhli.belajarspringdata.dtos.requests.RegisterUserDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.AuditDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserDetailsDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserListDTO;
import com.azkafadhli.belajarspringdata.entities.Audit;
import com.azkafadhli.belajarspringdata.entities.User;
import com.azkafadhli.belajarspringdata.repositories.IUserRepository;
import com.azkafadhli.belajarspringdata.utils.Sorter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public UserListDTO getUsers(GetUsersDTO getUsersDTO) {
        Sort sort = Sort.by(Sorter.getSortOrder(getUsersDTO.getSort()));
        Pageable pageable = PageRequest.of(getUsersDTO.getPage(), getUsersDTO.getLimit(), sort);
        Page<User> userPage = userRepository.findAll(pageable);
        PaginationDTO paginationDTO = new PaginationDTO(userPage);
        List<UserDTO> userList = userPage
                .getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
        return new UserListDTO(userList, paginationDTO);
    }

    @Override
    public void addUser(RegisterUserDTO userRequest) {
        if (userRepository.existsByEmailOrUsername(userRequest.getEmail(), userRequest.getUsername())) {
            throw new DataIntegrityViolationException("email or username already used");
        }

        User user = modelMapper.map(userRequest, User.class);
        user.setAudit(new Audit());
        userRepository.save(user);
    }

    @Override
    public UserDetailsDTO getUserById(UUID id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));

        UserDetailsDTO userDetailsDTO = modelMapper.map(user, UserDetailsDTO.class);
        userDetailsDTO.setAudit(modelMapper.map(user.getAudit(), AuditDTO.class));

        return userDetailsDTO;
    }
}
