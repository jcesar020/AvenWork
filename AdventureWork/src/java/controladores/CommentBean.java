package controladores;

import cl.utils.HibernateUtil;
import entidades.*;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import javax.inject.Named;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author jcesa
 */
@Named(value = "commentBean")
@RequestScoped
public class CommentBean implements Serializable {

    private User user;
    private String usuario;

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUsuario() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
            this.user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
            usuario = user.getUser();
        }
        return usuario;
    }


    public User getUser() {

//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
//
//            if (this.user == null) {
//                this.user = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
//            }
//        } else {
//                this.user=new User();
//        }
        return user;
    }

    private Comment comentario;

    public Comment getComentario() {
        return comentario;
    }

    public void setComentario(Comment comentario) {
        this.comentario = comentario;
    }

    public void publicarComentario() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Transaction tx = ses.beginTransaction();

        //Setea el usuario
        User user_sel;
        user_sel = (User) ses.get(User.class, user.getUser());

        //Setear la foto
        Photo photo_sel;
        photo_sel = (Photo) ses.get(Photo.class, this.photoId);

        Comment Com = new Comment(photo_sel, user_sel, comentario.getSubject(), comentario.getBody());
        ses.saveOrUpdate(Com);
        tx.commit();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Comentario Almacenado");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        requestComentarios();
        comentario.setSubject("");
        comentario.setBody("");

    }

    private int photoId;

    private List<Comment> comentarios;

    public List<Comment> getComentarios() {

        return comentarios;

    }

    public void setComentarios(List<Comment> comentarios) {
        this.comentarios = comentarios;
    }

    private void requestComentarios() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();

        Query query = ses.createQuery("From Comment where PhotoId=:photoid");
        query.setInteger("photoid", photoId);
        comentarios = query.list();
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
        searchPhoto();
        requestComentarios();
    }

    private Photo photo;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;

    }

    public CommentBean() {
        photo = new Photo();
        user = new User();
        comentario = new Comment();

    }

    public void searchPhoto() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Transaction tx = ses.beginTransaction();

        Photo photo_sel;
        photo_sel = (Photo) ses.get(Photo.class, this.photoId);
        photo = photo_sel;

    }

}
