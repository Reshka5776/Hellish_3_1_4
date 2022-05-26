package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    public void adduser(User user) {
        entityManager.persist(user);
    }

    public void removeUser(long id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    public void editUser(User updatedUser) {
        entityManager.merge(updatedUser);
    }

    public User getUserByEmail(String email) {
         return entityManager.createQuery("SELECT u FROM User u WHERE u.email = ?1", User.class)
                 .setParameter(1, email)
                 .getSingleResult();
    }
}
