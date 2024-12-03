package com.example.nrfaboekhoudapplicatie.dal.implementatie;

import com.example.nrfaboekhoudapplicatie.dal.entity.User;
import com.example.nrfaboekhoudapplicatie.dal.repository.UserRepository;
import com.example.nrfaboekhoudapplicatie.service.interfaces.IUserDAL;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAL implements IUserDAL {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

    public UserDAL(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsernameAndRole(String username, String role) {
        System.out.println("Searching for user with username: " + username + " and role: " + role);
        String jpql = """
        SELECT u 
        FROM User u 
        JOIN u.roles r 
        WHERE u.username = :username AND r.name = :role
    """;

        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("username", username);
        query.setParameter("role", role);

        List<User> result = query.getResultList();
        System.out.println("Query result: " + result);

        return result.stream().findFirst();
    }

    @Override
    public Optional<User> findClientByUsername(String username) {
        return findByUsernameAndRole(username, "CLIENT");
    }

    @Override
    public Optional<User> findAccountantByUsername(String username) {
        return findByUsernameAndRole(username, "ACCOUNTANT");
    }

    @Override
    public boolean existsByUsername(String username) {
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.username = :username";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
