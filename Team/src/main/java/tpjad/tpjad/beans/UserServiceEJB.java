package tpjad.tpjad.beans;

import tpjad.tpjad.dtos.UserDTO;
import tpjad.tpjad.entities.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserServiceEJB {
    @PersistenceContext(unitName = "Store")
    private EntityManager entityManager;
    public void createUser(UserDTO userDTO) {
        User userEntity = new User( userDTO.getPassword(), userDTO.getName(), userDTO.getEmail(), userDTO.getPhone(), userDTO.getAddress());
        entityManager.persist(userEntity);
    }

    public UserDTO getUserById(Long userId) {
        User userEntity = entityManager.find(User.class, userId);
        if (userEntity != null) {
            return new UserDTO(userEntity.getPassword(), userEntity.getName(), userEntity.getEmail(), userEntity.getPhone(), userEntity.getAddress());
        }
        return null;
    }
    public Long verifyUserCredentials(String email, String password) {
        try {
            User user = (User) entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            return user.getId();
        } catch (Exception e) {
            return null;
        }
    }
    public User findUserById(Long userId) {
        return entityManager.find(User.class, userId);
    }
}

