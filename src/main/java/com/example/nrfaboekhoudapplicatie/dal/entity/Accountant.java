package com.example.nrfaboekhoudapplicatie.dal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "accountants")
@ToString(onlyExplicitlyIncluded = true) // Alleen expliciete velden in de toString
public class Accountant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; // Niet opgenomen in toString

    @OneToMany(mappedBy = "accountant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Client> clients = new HashSet<>(); // Niet opgenomen in toString
}
