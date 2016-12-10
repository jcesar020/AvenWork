/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import cl.utils.HibernateUtil;
import entidades.User;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import modelos.userDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author jcesa
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
        user = new User();
    }

    public String guardar() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Transaction tx = ses.beginTransaction();

        User use = new User(user.getUser(), user.getName(), user.getPassword());
        ses.saveOrUpdate(use);
        tx.commit();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Fotografia Almacenada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        user.setUser("");
        user.setName("");
        user.setPassword("");

        return "login?faces-redirect=true";
    }

    public String validar() throws Exception {
        userDAO userdao = new userDAO();
        User us = null;
        String resultado = "";
        try {
            us = userdao.validar(this.user);
            if (us != null) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", us);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login", "Bienvenido");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                resultado = "gallery";
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Datos erroneos");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                resultado = "error";
            }
        } catch (Exception e) {
            throw e;
        }
        return resultado + "?faces-redirect=true";
    }

    public boolean verificarSesion() {
        boolean estado = false;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
            estado = true;
        }
        return estado;
    }

    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }
    
    public String abrirSesion(){
        return "login?faces-redirect=true";
    }

}
