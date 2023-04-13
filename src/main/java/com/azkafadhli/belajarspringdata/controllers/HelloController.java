package com.azkafadhli.belajarspringdata.controllers;

import com.azkafadhli.belajarspringdata.dtos.responses.HelloDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<HelloDTO> getHello() {
        HelloDTO helloDTO = new HelloDTO();
        helloDTO.setMessage("Hello");
        return ResponseEntity.ok().body(helloDTO);
    }

}
