package models;

//PlayBean (JDBC and ORM)

import com.avaje.ebean.Model;

import javax.persistence.*;

@Entity
public class Usersys extends Model {

    @Id
    private Integer id;

    @Column(length = 128)
    private String username;

    @Column(length = 128)
    private String userpass;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public static Finder<Integer, Usersys> find = new Model.Finder<>(Usersys.class);
}