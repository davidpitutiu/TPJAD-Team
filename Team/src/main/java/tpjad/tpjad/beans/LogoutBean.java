package tpjad.tpjad.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;

@Named
@RequestScoped
public class LogoutBean {

    public String logout() {
        System.out.println("Invalidating session for user.");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        System.out.println("Session invalidated.");
        return "/index?faces-redirect=true";
    }

}
