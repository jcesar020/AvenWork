/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import cl.utils.HibernateUtil;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
        user=new User();
    }
    
    public void guardar(){
        SessionFactory sf= HibernateUtil.getSessionFactory();
        Session ses=sf.openSession();
        Transaction tx=ses.beginTransaction();
        User use =new User( user.getUser(), user.getName(), user.getPassword());
        ses.saveOrUpdate(use);
        tx.commit();
        FacesMessage msg=new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso","Fotografia Almacenada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        user.setUser("");
        user.setName("");
        user.setPassword("");
        
        //return "index";
      
    }
    
}
