package com.example.nrfaboekhoudapplicatie.dal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clients")
@ToString(onlyExplicitlyIncluded = true) // Lombok genereert alleen expliciet opgenomen velden
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include // Dit veld opnemen in de toString
    private Long id;

    @Column(nullable = false)
    @ToString.Include
    private String firstName;

    @Column(nullable = false)
    @ToString.Include
    private String lastName;

    @Column(nullable = false, unique = true)
    @ToString.Include
    private String email;

    @ManyToOne
    @JoinColumn(name = "accountant_id", nullable = false)
    private Accountant accountant; // Niet opgenomen in toString

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; // Niet opgenomen in toString
}
