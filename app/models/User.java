package models;

//PlayBean (JDBC and ORM)
import com.avaje.ebean.Model;
import javax.persistence.*;

@Entity
public class User extends Model{

    @Id
    private Integer id;

    @Column(length = 128)
    private String username;

    @Column(length = 128)
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Finder<Integer, User> find = new Model.Finder<>(User.class);
}