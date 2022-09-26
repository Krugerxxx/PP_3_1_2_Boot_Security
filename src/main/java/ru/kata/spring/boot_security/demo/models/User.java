package ru.kata.spring.boot_security.demo.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2, message = "Имя не может быть меньше 2х символов")
    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Min(value = 0, message = "Возраст не может быть меньше 0")
    @Max(value = 120, message = "Возраст не может быть больше 120")
    @Column(name = "age", nullable = false, length = 100)
    private int age;

    @NotEmpty(message = "E-mail не может быть пустым")
    @Email(message = "Неверный e-mail")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 3, message = "Пароль не может быть меньше 3х символов")
    @Column(name = "password", nullable = false)
    private String password;

    @NotEmpty(message = "Выберете роль")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }


}
