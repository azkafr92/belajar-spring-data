package com.azkafadhli.belajarspringdata.controllers;

import com.azkafadhli.belajarspringdata.constants.Constant;
import com.azkafadhli.belajarspringdata.dtos.requests.GetUsersDTO;
import com.azkafadhli.belajarspringdata.dtos.requests.RegisterUserDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserDetailsDTO;
import com.azkafadhli.belajarspringdata.dtos.responses.UserListDTO;
import com.azkafadhli.belajarspringdata.services.IUserService;
import com.azkafadhli.belajarspringdata.utils.PageAndSortMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterUserDTO userRequest) {
        // TODO: check username & email, no user with same username or email

        userService.addUser(userRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping
    public ResponseEntity<UserListDTO> getUsers(
            @RequestParam(defaultValue = Constant.DEFAULT_PAGE_NUMBER, name="page") String inputPage,
            @RequestParam(defaultValue = Constant.DEFAULT_PAGE_SIZE, name="limit") String inputLimit,
            @RequestParam(required = false)String[] sort,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {

        GetUsersDTO getUsersDTO = new GetUsersDTO();
        PageAndSortMapper.map(inputPage, inputLimit, sort, getUsersDTO);

        // TODO: filter by username, email

        UserListDTO users = userService.getUsers(getUsersDTO);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsDTO> getUserById(@PathVariable UUID id) {
        UserDetailsDTO user = userService.getUserById(id);
        return ResponseEntity.ok().body(user);
    }

}
