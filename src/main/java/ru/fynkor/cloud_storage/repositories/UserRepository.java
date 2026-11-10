package ru.fynkor.cloud_storage.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fynkor.cloud_storage.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    boolean existsUserByUsername(String username);
    Optional<User> findUserByUsername(String username);
}
