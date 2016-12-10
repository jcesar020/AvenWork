package controladores;

import cl.utils.HibernateUtil;
import entidades.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import modelos.CommentDAO;
import modelos.PhotoDAO;
import org.apache.commons.io.IOUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

public class PhotosBean {

    private Photo photo = new Photo();
    private UploadedFile file;
    private Comment comentario = new Comment();

    public Comment getComentario() {
        return comentario;
    }

    public void setComentario(Comment comentario) {
        this.comentario = comentario;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public PhotosBean() {

    }

    public List<Photo> getPhotos() throws Exception {
        User user_sel = new User();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
            user_sel = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        }

        PhotoDAO photoDAO = new PhotoDAO();
        List<Photo> lista;
        lista = photoDAO.listar(user_sel.getUser());
        return lista;
    }

    public List<Photo> getLastPhotos() throws Exception {
        User user_sel = new User();
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario") != null) {
//            user_sel = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
//        }

        PhotoDAO photoDAO = new PhotoDAO();
        List<Photo> lista;
        lista = photoDAO.listar(2,5);
        return lista;
    }

    public StreamedContent getImagen() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String imageId = context.getExternalContext().getRequestParameterMap().get("imageId");
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session ses = sf.openSession();
            Photo custo = (Photo) ses.get(Photo.class, Integer.valueOf(imageId));
            InputStream input = new ByteArrayInputStream(custo.getPhotoFile());
            return new DefaultStreamedContent(input, "image/jpg");
        }
    }

    public String guardar() throws IOException, Exception {
        User user_sel = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        PhotoDAO photoDAO = new PhotoDAO();
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Transaction tx = ses.beginTransaction();

        //Agregar la imagen
        if (file != null) {
            byte[] targetArray = IOUtils.toByteArray(file.getInputstream());
            photo.setPhotoFile(targetArray);
        }

        Photo pho = new Photo(user_sel, photo.getTitle(), photo.getDescripcion(), photo.getPhotoFile());
        photoDAO.agregar(pho);

        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Fotografia Almacenada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        photo.setTitle("");
        photo.setDescripcion("");
        return "galery";
    }

    ///////////////////MOFIFICACIONES////////////////////
    public String leer(Photo photo) {
        this.photo = photo;
        return "photo";
    }

    public String eliminar() throws Exception {
        PhotoDAO photoDAO = new PhotoDAO();
        photoDAO.eliminar(this.photo);
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "ELIMINACION", "Fotografia Eliminada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "galery";
    }

    public void publicarComentario() {
        User user_sel = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        CommentDAO commentDAO = new CommentDAO();

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Transaction tx = ses.beginTransaction();

        //Setear la foto
        Photo photo_sel;
        photo_sel = (Photo) ses.get(Photo.class, this.photo.getPhotoId());

        Comment com = new Comment(photo_sel, user_sel, comentario.getSubject(), comentario.getBody());

        if (commentDAO.agregarComentario(com)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Comentario Almacenado");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            photo.getComments().add(com);

            //requestComentarios();
            comentario.setSubject("");
            comentario.setBody("");
        }
    }
}
