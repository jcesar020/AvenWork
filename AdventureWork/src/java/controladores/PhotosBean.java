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
import org.apache.commons.io.IOUtils;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

public class PhotosBean {

    private Photo photo;
    private UploadedFile file;
    private InputStream photografy;

    public InputStream getPhotografy() {
        return photografy;
    }

    public void setPhotografy(InputStream photografy) {
        this.photografy = photografy;
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
        photo = new Photo();
    }

    public List<Photo> getPhotos() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Query query = ses.createQuery("From Photo where Owner=:idowner");
        query.setString("idowner","jcesar" ); //Falta crear la parte de login para utilizar ese parametro
        List<Photo> lista=query.list();
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

    public void guardar() throws IOException {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session ses = sf.openSession();
        Transaction tx = ses.beginTransaction();

        //Setea el usuario
        User user_sel;
        user_sel = (User) ses.get(User.class, "jcesar");

        //Agregar la imagen
        if (file != null) {
            byte[] targetArray = IOUtils.toByteArray(file.getInputstream());
            photo.setPhotoFile(targetArray);
        }

        Photo pho = new Photo(user_sel, photo.getTitle(), photo.getDescripcion(), photo.getPhotoFile());
        ses.saveOrUpdate(pho);
        tx.commit();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Fotografia Almacenada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        photo.setTitle("");
        photo.setDescripcion("");
    }
}
