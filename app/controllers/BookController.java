package controllers;

import play.mvc.*;
import services.ErrorClass;
import services.Picture;
import services.Token;
import services.Validation;
import views.html.book.*;
import models.*;
import play.data.*;
import views.html.error._403;
import views.html.error._404;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class BookController extends Controller {

    private static String token = null;

    private Integer id = 0;

    private String isbn = "";

    @Inject
    FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public Result create(Integer id) {
        Author author = Author.find.byId(id);
        if (author != null) {
            this.id = author.getId();
            Form<Book> frm = formFactory.form(Book.class);
            BookController.GenerateToken();
            return ok(create.render(author, frm, new HashSet<ErrorClass>(), token));
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result save(Integer id) {

        if (!(session("token").toString().equals(token))) {
            return forbidden(_403.render());
        }

        Author author = Author.find.byId(id);
        if (author != null) {
            this.id = id;

            Form<Book> frm = formFactory.form(Book.class).bindFromRequest();

            if (frm.data().get("price").equals("")) {
                frm.get().setPrice(Double.parseDouble("0.0"));
            }
            Http.MultipartFormData<File> body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
            frm.get().setPicture(Picture.FileToByte(picture));

            Set<ErrorClass> errors = Validation.validateDataBook(1, frm.get());
            if (errors.isEmpty()) {
                Book book = frm.get();
                book.setPicture(Picture.FileToByte(picture));
                book.setAuthor(author);
                book.save();
                flash("success", "Book successfully created.");
                return redirect(routes.AuthorController.details(id));
            } else {
                BookController.GenerateToken();
                return badRequest(create.render(author, frm, errors, token));
            }
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result edit(Integer id, String isbn) {
        Author author = Author.find.byId(id);
        if (author != null) {
            this.id = author.getId();
            Book book = Book.find.where().eq("isbn", isbn).findUnique();
            if (book != null) {
                Form<Book> frm = formFactory.form(Book.class).fill(book);
                BookController.GenerateToken();
                return ok(edit.render(author, isbn, frm, new HashSet<>(), token));
            }
            return notFound(_404.render());
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result update(Integer id, String isbn) {

        if (!(session("token").toString().equals(token))) {
            return forbidden(_403.render());
        }

        Author author = Author.find.byId(this.id);
        if (author != null) {
            Book oldbook = Book.find.where().eq("isbn", isbn).findUnique();
            if (oldbook != null) {

                Form<Book> frm = formFactory.form(Book.class).bindFromRequest();

                if (frm.data().get("price").equals("")) {
                    frm.get().setPrice(Double.parseDouble("0"));
                }

                Http.MultipartFormData<File> body = request().body().asMultipartFormData();
                Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
                if (!(picture.getFilename().equals(""))) {
                    oldbook.setPicture(Picture.FileToByte(picture));
                }

                Set<ErrorClass> errors = Validation.validateDataBook(2, frm.get());

                if (errors.isEmpty()) {
                    oldbook.setTitle(frm.get().getTitle());
                    oldbook.setPrice(frm.get().getPrice());
                    oldbook.update();
                    flash("success", "Book successfully edited.");
                    return redirect(routes.AuthorController.details(this.id));
                } else {
                    BookController.GenerateToken();
                    return ok(edit.render(author, isbn, frm, errors, token));
                }
            } else {
                return notFound(_404.render());
            }
        } else {
            return notFound(_404.render());
        }
    }


    @Security.Authenticated(Secured.class)
    public Result delete(Integer id, String isbn) {

        if (!(session("token").toString().equals(token))) {
            return forbidden(_403.render());
        }

        Author author = Author.find.byId(id);
        if (author != null) {
            Book book = Book.find.where().eq("isbn", isbn).findUnique();
            if (book != null) {
                book.delete();
                flash("message", "Book successfully deleted.");
                return redirect(routes.AuthorController.details(id));
            }
            return notFound(_404.render());
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result details(Integer id, String isbn) {
        Author author = Author.find.byId(id);
        if (author != null) {
            Book book = Book.find.where().eq("isbn", isbn).findUnique();
            if (book != null) {
                BufferedImage pictureAuthor = Picture.ByteToFile(author.getPicture(), 1);
                BufferedImage pictureBook = Picture.ByteToFile(book.getPicture(), 2);
                return ok(details.render(book, author));
            }
            return notFound(_404.render());
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result picture(Integer id, String isbn) {

        if (!(session("token").toString().equals(token))) {
            return forbidden(_403.render());
        }

        Book book = Book.find.where().eq("isbn", isbn).findUnique();
        if (book != null) {
            return ok(book.getPicture());
        }
        return null;
    }

    private static void GenerateToken() {
        BookController.token = Token.generateToken();
        session().remove("token");
        session("token", BookController.token);
    }
}
