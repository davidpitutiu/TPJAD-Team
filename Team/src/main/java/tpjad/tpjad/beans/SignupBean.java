package tpjad.tpjad.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tpjad.tpjad.dtos.UserDTO;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

@Named
@RequestScoped
public class SignupBean {
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
    private String phone;
    private String address;

    @PersistenceContext(unitName = "Store")
    private EntityManager entityManager;

    @EJB
    private UserServiceEJB userEJB;


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    // Getter and setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String register() {
        StringBuilder jsScript = new StringBuilder();

        if (password == null || password.length() < 8) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password is too short. It must be at least 8 characters long.", null));
        }

        if (!password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Passwords do not match!", null));
            return null;
        }

        if (emailExists(email)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Email already registered.", null));
            return null;
        }

        if (jsScript.length() > 0) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("jsScript", jsScript.toString());
            return null;
        }

        UserDTO userDTO = new UserDTO(password, name, email, phone, address);
        try {
            userEJB.createUser(userDTO);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registration Successful!", null));
            return "signup";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed: " + e.getMessage(), null));
            return null;
        }
    }

    private boolean emailExists(String email) {
        try {
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email");
            query.setParameter("email", email);
            query.getSingleResult();
            return true; // Email found, return true
        } catch (NoResultException e) {
            return false; // No result found, email does not exist
        }
    }
}
