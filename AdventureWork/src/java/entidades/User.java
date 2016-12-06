package entidades;
// Generated 11-30-2016 01:02:38 AM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;

/**
 * User generated by hbm2java
 */
public class User  implements java.io.Serializable {


     private String user;
     private String name;
     private String password;
     private Set<Photo> photos = new HashSet<Photo>(0);
     private Set<Comment> comments = new HashSet<Comment>(0);

    public User() {
    }

	
    public User(String user, String name, String password) {
        this.user = user;
        this.name = name;
        this.password = password;
    }
    public User(String user, String name, String password, Set<Photo> photos, Set<Comment> comments) {
       this.user = user;
       this.name = name;
       this.password = password;
       this.photos = photos;
       this.comments = comments;
    }
   
    public String getUser() {
        return this.user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<Photo> getPhotos() {
        return this.photos;
    }
    
    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }
    public Set<Comment> getComments() {
        return this.comments;
    }
    
    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }




}


