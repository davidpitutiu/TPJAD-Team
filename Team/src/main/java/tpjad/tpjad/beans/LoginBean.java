package tpjad.tpjad.beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;

@Named
@RequestScoped
public class LoginBean {

    private String email;
    private String password;

    @EJB
    private UserServiceEJB userServiceEJB;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String login() {
        Long userId = userServiceEJB.verifyUserCredentials(email, password);
        if (userId != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userId", userId);
            return "home?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new jakarta.faces.application.FacesMessage("Login failed. Check your email and password."));
            return "index?faces-redirect=true";
        }
    }
}

