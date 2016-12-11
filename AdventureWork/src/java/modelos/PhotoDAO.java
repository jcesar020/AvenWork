package modelos;

import cl.utils.HibernateUtil;
import entidades.Photo;
import static java.lang.Math.ceil;
import java.util.HashSet;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

public class PhotoDAO {

    Session session;
    Transaction trans;
    private long count;
    private int pages;
    private int pagesize = 6;//Tamano por default de la pagina

    public int getPages() {
        countPhotos();
        return pages;
    }

    public long getCount() {
        countPhotos();
        return count;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public List<Photo> listar(String propie) throws Exception {
        session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("From Photo where Owner=:idowner");
        query.setString("idowner", propie); //Falta crear la parte de login para utilizar ese parametro
        List<Photo> lista = query.list();
        return lista;
    }

    public List<Photo> listar(int pag) throws Exception {
        int inicio = pagesize * (pag - 1);
        session = HibernateUtil.getSessionFactory().openSession();
        Criteria cr = session.createCriteria(Photo.class);
        cr.setFirstResult(inicio);
        cr.setMaxResults(pagesize);
        cr.addOrder(Order.desc("createdDate"));
        List<Photo> lista = cr.list();
        session.close();
        return lista;
    }

    private void countPhotos() {
        session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteriaCount = session.createCriteria(Photo.class);
        criteriaCount.setProjection(Projections.rowCount());
        this.count = (long) criteriaCount.uniqueResult();
        this.pages = (int) ceil(count / pagesize);
    }

    public void agregar(Photo photo) throws Exception {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.saveOrUpdate(photo);
            trans.commit();
        } catch (HibernateException e) {
            trans.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void eliminar(int id) throws Exception {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Photo photo = (Photo) session.get(Photo.class, id);
            trans = session.beginTransaction();
            session.delete(photo);
            trans.commit();
        } catch (HibernateException e) {
            trans.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void actualizar(Photo photo) throws Exception {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            trans = session.beginTransaction();
            session.saveOrUpdate(photo);
            trans.commit();
        } catch (HibernateException e) {
            throw e;
        } finally {
        }
    }
}
