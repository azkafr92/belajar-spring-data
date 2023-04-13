package com.azkafadhli.belajarspringdata.dtos.responses;

import com.azkafadhli.belajarspringdata.entities.Address;
import com.azkafadhli.belajarspringdata.entities.Authority;
import com.azkafadhli.belajarspringdata.entities.UserIdentity;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetailsDTO implements Serializable {
    private UUID id;
    private String email;
    private String username;
    private Boolean isEnabled;
    private List<Address> addresses;
    private Set<Authority> authorities;
    private UserIdentity userIdentity;
    private AuditDTO audit;
}
