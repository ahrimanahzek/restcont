package com.kang.restcont.dao;

import com.kang.restcont.model.Role;
import com.kang.restcont.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;


public interface UserDAO {

    List<User> readAll();

    List<Role> readRole();

    Set<Role> getRoles(String[] ids);

    void insert(User user);

    void update(User user);

    void delete(int id);

    User read(Integer id);

    UserDetails findByUsername(String username);
}