package com.azkafadhli.belajarspringdata.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is mandatory")
    private String email;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(columnDefinition = "boolean default false")
    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_tags",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Address> addresses;

    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private UserIdentity userIdentity;

    @Embedded
    private Audit audit;

    @PrePersist
    public void fillCreatedOn() {
        audit.setCreatedOn(LocalDateTime.now());
    }

    @PreUpdate
    public void fillUpdatedOn() {
        audit.setUpdatedOn(LocalDateTime.now());
    }

}

