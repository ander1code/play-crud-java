package models;

//PlayBean (JDBC and ORM)
import javax.persistence.*;
import javax.validation.constraints.*;
import com.avaje.ebean.*;

@Entity
public class Book extends Model {

    @Id
    private Integer id;

    @ManyToOne
    private Author author;

    @Column(length = 13, unique = true)
    private String isbn;

    @Column(length = 100)
    private String title;

    @Column(precision = 12, scale = 2)
    private Float price;

    @Lob
    private byte[] picture;

    public Book () {
    }

    public Book (Integer id, Author author, String isbn, String title, Float price, byte[] picture) {
        this.setId (id);
        this.setAuthor (author);
        this.setIsbn (isbn);
        this.setTitle (title);
        this.setPrice (price);
        this.setPicture (picture);
    }

    public Integer getId () {
        return id;
    }

    public void setId (Integer id) {
        this.id = id;
    }

    public String getIsbn () {
        return isbn;
    }

    public void setIsbn (String isbn) {
        this.isbn = isbn;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public Float getPrice () {
        return price;
    }

    public void setPrice (Float price) {
        this.price = price;
    }

    public byte[] getPicture () {
        return picture;
    }

    public void setPicture (byte[] picture) {
        this.picture = picture;
    }

    public Author getAuthor () {
        return author;
    }

    public void setAuthor (Author author) {
        this.author = author;
    }

    public static final Finder<Long, Book> find = new Finder<>(Book.class);

    //Complexos.
    public static Book getBookByISBN (String isbn) {
        return null;
    }

    public static BookAuthor getBookAuthorByISBN (String isbn) {
        return null;
    }

}
