package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserRepo;
import ru.kata.spring.boot_security.demo.exeption_handling.EmailExistsException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getId() != null && user.getPassword() == "") {
            user.setPassword(userRepo.findById(user.getId()).get().getPassword());
        } else {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (!existEmail(user.getEmail()) ||
                (existEmail(user.getEmail()) && findByUsername(user.getEmail()).getId() == user.getId())) {
            userRepo.save(user);
        } else {
            throw new EmailExistsException();
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        Set<User> userSet = new HashSet<>();
        userRepo.findAll().forEach(n -> userSet.add(n));
        return userSet.stream()
                .sorted((n, m) -> (int) (n.getId() - m.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        return userRepo.findById(id).get();
    }

    @Override
    public User findByUsername(String name) {
        return findAll().stream().filter(n -> n.getEmail().equals(name)).findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(n -> new SimpleGrantedAuthority(n.getName())).collect(Collectors.toList());
    }

    private boolean existEmail(String email) {
        if (findByUsername(email) != null) {
            return true;
        }
        return false;
    }

}
