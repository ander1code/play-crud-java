package models;

//PlayBean (JDBC and ORM)
import com.avaje.ebean.*;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Author extends Model{

    @Id
    private Integer id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String artisticName;

    @Column(length = 100, unique = true)
    private String email;

    private Date birthday;

    @Column(length = 1)
    private String gender;

    @Column(length = 1000)
    private String biography;

    @Lob
    private byte[] picture;

    public Author () {
    }

    public Author (Integer id, String name, String artisticName, String email, Date birthday, String gender, String biography, byte[] picture) {
        this.setId (id);
        this.setName (name);
        this.setArtisticName (artisticName);
        this.setEmail (email);
        this.setBirthday (birthday);
        this.setGender (gender);
        this.setBiography (biography);
        this.setPicture (picture);
    }

    public Integer getId () {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getArtisticName () {
        return artisticName;
    }

    public void setArtisticName (String artisticName) {
        this.artisticName = artisticName;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public Date getBirthday () {
        return birthday;
    }

    public void setBirthday (Date birthday) {
        this.birthday = birthday;
    }

    public String getGender () {
        return gender;
    }

    public void setGender (String gender) {
        this.gender = gender;
    }

    public String getBiography () {
        return biography;
    }

    public void setBiography (String biography) {
        this.biography = biography;
    }

    public byte[] getPicture () {
        return this.picture;
    }

    public void setPicture (byte[] picture) {

        this.picture =  picture;
    }

    public static Finder<Integer, Author> find = new Finder<>(Author.class);

    public static Author getAuthorBydId (Integer id) {
        return null;
    }
}