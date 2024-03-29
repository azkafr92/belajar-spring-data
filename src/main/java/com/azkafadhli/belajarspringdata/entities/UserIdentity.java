package com.azkafadhli.belajarspringdata.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "user_identities")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserIdentity {

    @Id
    @Column(name = "user_id")
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 16)
    private String nik;

    private String firstName;

    private String lastName;

    private Date dob;

}
