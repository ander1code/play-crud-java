package controllers;

import models.Author;
import models.Book;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.ErrorClass;
import services.Picture;
import services.Token;
import services.Validation;
import views.html.author.create;
import views.html.author.details;
import views.html.author.edit;
import views.html.author.index;
import views.html.error.*;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

public class AuthorController extends Controller {

    @Inject
    FormFactory formFactory;

    private static String token = null;

    private Integer id = null;

    @Security.Authenticated(Secured.class)
    public Result index() {
        List<Author> authors = Author.find.all();
        authors.sort(Comparator.comparing(Author::getId).reversed());
        return ok(index.render(authors));
    }

    @Security.Authenticated(Secured.class)
    public Result create() {
        generateToken();
        Form<Author> frm = formFactory.form(Author.class);
        return ok(create.render(frm, new HashSet<>(), token));
    }

    @Security.Authenticated(Secured.class)
    public Result save() {

        Form<Author> frm = formFactory.form(Author.class).bindFromRequest();

        String sessionToken = session("token");
        String formToken = frm.data().get("token");
        if (sessionToken == null || formToken == null || !sessionToken.equals(formToken)) {
            return forbidden(_403.render());
        }

        frm.get().setEmail(frm.get().getEmail().toLowerCase());

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        frm.get().setPicture(Picture.FileToByte(picture));

        Set<ErrorClass> errors = Validation.validateDataAuthor(1, frm.get());

        if (errors.isEmpty()) {
            Author author = frm.get();
            author.setPicture(Picture.FileToByte(picture));
            author.save();
            flash("success", "Author successfully created.");
            return redirect(routes.AuthorController.index());
        } else {
            generateToken();
            return badRequest(create.render(frm, errors, token));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result edit(Integer id) {
        Author author = Author.find.byId(id);
        if (author != null) {
            this.id = id;
            BufferedImage file = Picture.ByteToFile(author.getPicture(), 1);
            Form<Author> frm = formFactory.form(Author.class).fill(author);
            AuthorController.generateToken();
            return ok(edit.render(frm, new HashSet<>(), AuthorController.token));
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result update() {
        Form<Author> frm = formFactory.form(Author.class).bindFromRequest();

        String sessionToken = session("token");
        String formToken = frm.data().get("token");
        if (!sessionToken.equals(formToken)) {
            return forbidden(_403.render());
        }

        Author oldAuthor = Author.find.byId(this.id);
        if (oldAuthor == null) {
            return notFound(_404.render());
        }

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        frm.get().setPicture(Picture.FileToByte(picture));

        Author newAuthor = frm.get();
        Set<ErrorClass> errors;

        if (!newAuthor.getEmail().equals(oldAuthor.getEmail())) {
            errors = Validation.validateDataAuthor(1, frm.get());
        } else {
            errors = Validation.validateDataAuthor(2, frm.get());
        }

        if (!errors.isEmpty()) {
            AuthorController.generateToken();
            return badRequest(edit.render(frm, errors, AuthorController.token));
        }

        oldAuthor.setName(newAuthor.getName());
        // oldAuthor.setEmail(newAuthor.getEmail().toLowerCase());
        oldAuthor.setBirthday(newAuthor.getBirthday());
        oldAuthor.setBiography(newAuthor.getBiography());
        oldAuthor.setArtisticName(newAuthor.getArtisticName());
        oldAuthor.setGender(newAuthor.getGender());

        if (picture != null && !picture.getFilename().isEmpty()) {
            oldAuthor.setPicture(Picture.FileToByte(picture));
        }

        oldAuthor.update();
        flash("success", "Author successfully edited.");
        return redirect(routes.AuthorController.details(id));
    }

    @Security.Authenticated(Secured.class)
    public Result delete() {

        String sessionToken = session("token");
        if (sessionToken == null || !sessionToken.equals(token)) {
            return forbidden(_403.render());
        }

        Author author = Author.find.byId(this.id);
        if (author != null) {
            if (Validation.deleteAuthor(id)) {
                author.delete();
                flash("success", "Author successfully deleted.");
            } else {
                flash("danger", "Author has one or more books registered and cannot be deleted.");
            }
            return redirect(routes.AuthorController.index());
        } else {
            return notFound(_404.render());
        }
    }

    @Security.Authenticated(Secured.class)
    public Result details(Integer id) {
        Author author = Author.find.byId(id);
        if (author != null) {
            BufferedImage file = Picture.ByteToFile(author.getPicture(), 1);
            List<Book> books = Book.find.where().eq("author_id", id).findList();
            return ok(details.render(author, books, file));
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result picture(Integer id) {
        Author author = Author.find.byId(id);
        if (author != null) {
            return ok(author.getPicture());
        }
        return notFound(_404.render());
    }

    private static void generateToken() {
        token = Token.generateToken();
        Http.Context.current().session().remove("token");
        Http.Context.current().session().put("token", token);
    }
}