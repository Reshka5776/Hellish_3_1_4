package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRoleById(long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void update(Role updatedRole) {
        entityManager.merge(updatedRole);
    }
    @Override
    public void removeRole(long id) {
        entityManager.remove(entityManager.find(Role.class, id));
    }
    @Override
    public Set<Role> getRoles() {
        return entityManager.createQuery("select role from Role role", Role.class).getResultStream().collect(Collectors.toSet());
    }
}
