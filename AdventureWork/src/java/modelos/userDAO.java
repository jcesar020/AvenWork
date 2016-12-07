/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import cl.utils.HibernateUtil;
import entidades.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class userDAO {

    public User validar(User usuario) throws Exception {
        User us = null;

        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session ses = sf.openSession();

            Query query = ses.createQuery("From User where user= :userid and password=:pass");
            query.setString("userid", usuario.getUser());
            query.setString("pass", usuario.getPassword());

            if (!query.list().isEmpty()) {
                us = (User) query.list().get(0);
            }

        } catch (Exception e) {
            throw e;
        }
        return us;
    }

}
