/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import cl.utils.HibernateUtil;
import entidades.Photo;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

/**
 *
 * @author jcesa
 */
public class PhotoDAO {

    Session session;
    Transaction trans;

    public List<Photo> listar(String propie) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("From Photo where Owner=:idowner");
        query.setString("idowner", propie); //Falta crear la parte de login para utilizar ese parametro
        List<Photo> lista = query.list();

        return lista;
    }

    public List<Photo> listar(int inicio, int cant) throws Exception {
        
        
        session = HibernateUtil.getSessionFactory().openSession();
        Criteria cr=session.createCriteria(Photo.class);
        cr.setFirstResult(inicio);
        cr.setMaxResults(cant);
        cr.addOrder(Order.desc("createdDate"));
        
//        
//        Query query = session.createQuery("From Photo where Owner=:idowner");
//        query.setString("idowner", propie); //Falta crear la parte de login para utilizar ese parametro
//        
        List<Photo> lista = cr.list();

        return lista;
    }

    public void agregar(Photo photo) throws Exception {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.saveOrUpdate(photo);
            trans.commit();
        } catch (Exception e) {
            trans.rollback();
            throw e;
        }
    }

    public void eliminar(Photo photo) throws Exception {
        try {

            //session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.delete(photo);
            trans.commit();
            session.close();
        } catch (Exception e) {
            trans.rollback();
            throw e;
        }

    }

    public void actualizar(Photo photo) throws Exception {

    }
}
