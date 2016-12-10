/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import cl.utils.HibernateUtil;
import entidades.Comment;
import entidades.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CommentDAO {

    public boolean agregarComentario(Comment com) {
        boolean respuesta = false;
        try {
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session ses = sf.openSession();
            Transaction tx = ses.beginTransaction();
            ses.saveOrUpdate(com);

            tx.commit();
            ses.close();
            respuesta = true;
        } catch (Exception e) {
            throw e;
        }
        return respuesta;

    }

}
