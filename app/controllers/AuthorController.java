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


public class AuthorController extends Controller {

    @Inject
    FormFactory formFactory;

    private static String token = null;

    @Security.Authenticated(Secured.class)
    public Result index() {
        List<Author> authors = Author.find.all();
        return ok(index.render(authors));
    }

    @Security.Authenticated(Secured.class)
    public Result create() {
        Form<Author> frm = formFactory.form(Author.class);
        AuthorController.GenerateToken();
        return ok(create.render(frm, new HashSet<>(), token));
    }

    @Security.Authenticated(Secured.class)
    public Result save() {
        Form<Author> frm = formFactory.form(Author.class).bindFromRequest();

        if (!(session("token").toString().equals(frm.data().get("token").toString()))) {
            return forbidden(_403.render());
        }

        frm.get().setEmail(frm.get().getEmail().toLowerCase());

        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        Date birthday = frm.get().getBirthday();
        Set<ErrorClass> errors = Validation.ValidateDataAuthor(frm.get().getName(), frm.get().getArtisticName(), frm.get().getEmail(), birthday, frm.get().getGender(), frm.get().getBiography(), Picture.FileToByte(picture));

        if (errors.isEmpty()) {
            Author author = frm.get();
            author.setPicture(Picture.FileToByte(picture));
            author.save();
            flash("success", "Author successfully registered.");
            return redirect(routes.AuthorController.index());
        } else {
            AuthorController.GenerateToken();
            return badRequest(create.render(frm, errors, AuthorController.token));
        }
    }

    @Security.Authenticated(Secured.class)
    public Result edit(Integer id) {
        Author author = Author.find.byId(id);
        if (author != null) {
            BufferedImage file = Picture.ByteToFile(author.getPicture(), 1);
            Form<Author> frm = formFactory.form(Author.class).fill(author);
            AuthorController.GenerateToken();
            return ok(edit.render(frm, new HashSet<>(), AuthorController.token));
        }
        return notFound(_404.render());
    }

    @Security.Authenticated(Secured.class)
    public Result update(Integer id) {
        Form<Author> frm = formFactory.form(Author.class).bindFromRequest();

        if (!(session("token").toString().equals(frm.data().get("token").toString()))) {
            return forbidden(_403.render());
        }

        Author oldauthor = Author.find.byId(id);
        if (!(oldauthor == null)) {
            Http.MultipartFormData<File> body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
            Set<ErrorClass> errors = null;

            if (!(frm.get().getEmail().equals(oldauthor.getEmail()))) {
                errors = Validation.ValidateDataAuthor(frm.get().getName(), frm.get().getArtisticName(), frm.get().getEmail(), frm.get().getBirthday(), frm.get().getGender(), frm.get().getBiography());
            } else {
                errors = Validation.ValidateDataAuthor(frm.get().getName(), frm.get().getArtisticName(), frm.get().getBirthday(), frm.get().getGender(), frm.get().getBiography());
            }

            if (errors.isEmpty()) {

                oldauthor.setName(frm.get().getName());
                oldauthor.setEmail(frm.get().getEmail().toLowerCase());
                oldauthor.setBirthday(frm.get().getBirthday());
                oldauthor.setBiography(frm.get().getBiography());
                oldauthor.setArtisticName(frm.get().getArtisticName());
                oldauthor.setGender(frm.get().getGender());

                if (!(picture.getFilename().equals(""))) {
                    oldauthor.setPicture(Picture.FileToByte(picture));
                }

                oldauthor.update();
                flash("success", "Author successfully edited.");
                return redirect(routes.AuthorController.details(id));
            } else {
                AuthorController.GenerateToken();
                return badRequest(edit.render(frm, errors, AuthorController.token));
            }
        } else {
            return notFound(_404.render());
        }
    }

    @Security.Authenticated(Secured.class)
    public Result delete(Integer id) {

        if (!(session("token").toString().equals(AuthorController.token))) {
            return forbidden(_403.render());
        }

        Author author = Author.find.byId(id);
        if (!(author == null)) {
            if (Validation.DeleteAuthor(id)) {
                author.delete();
                flash("success", "Author successfully deleted.");
            } else {
                flash("danger", "Author has one or more books registered and don't can be deleted.");
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
            List<Book> books = Book.find.where().eq("author_id ", id).findList();
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

    private static void GenerateToken(){
        AuthorController.token = Token.generateToken();
        session().remove("token");
        session("token", AuthorController.token);
    }
}
