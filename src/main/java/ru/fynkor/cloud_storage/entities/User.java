package ru.fynkor.cloud_storage.entities;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Users")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username",
            unique = true,
            nullable = false,
            length = 20
    )
    private String username;

    @Column(name = "password",
        nullable = false
    )
    private String password;

    protected User(){}

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}
