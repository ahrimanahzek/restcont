package com.kang.restcont.dao;

import com.kang.restcont.model.Role;
import com.kang.restcont.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Repository
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> readAll() {
        TypedQuery<User> query = entityManager.createQuery("from User", User.class);
        return query.getResultList();
    }

    @Override
    public List<Role> readRole() {
        TypedQuery<Role> query = entityManager.createQuery("from Role", Role.class);
        return query.getResultList();
    }

    @Override
    public Set<Role> getRoles(String[] ids) {
        TypedQuery<Role> query = entityManager.createQuery("from Role where id = :id", Role.class);
        Set<Role> roles = new HashSet<>();

        Arrays.stream(ids).forEach(roleId ->
        {query.setParameter("id", Integer.parseInt(roleId)); roles.add(query.getSingleResult());});

        return roles;
    }

    @Override
    public void insert(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

    @Override
    public User read(Integer id) {
        TypedQuery<User> query = entityManager.createQuery("from User where id = :id", User.class);
        query.setParameter("id", id);

        return query.getSingleResult();
    }

    @Override
    public void delete(Integer id) {
        entityManager.remove(read(id));
    }

    @Override
    public UserDetails findByUsername(String login) {
        TypedQuery<User> query = entityManager.createQuery("from User where login = :login", User.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

}
