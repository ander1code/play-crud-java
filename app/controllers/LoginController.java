package controllers;

import play.mvc.*;

import play.data.*;

import javax.inject.Inject;

import services.ErrorClass;
import services.Token;
import services.Validation;

import views.html.error.*;

import views.html.login.*;

import views.html.*;

import java.security.MessageDigest;
import java.util.HashSet;

import java.util.Set;

import models.*;


import services.HashUtil.*;

public class LoginController extends Controller {

    private static String token = null;

    public static class Function {
        public static boolean CheckSession() {
            String user = session("connected");
            if (user != null) {
                return true;
            } else {
                return false;
            }
        }

        public static String getUsername() {
            return session("connected");
        }
    }

    @Inject
    FormFactory formFactory;

    public Result login() {
        try {
            Usersys user = new Usersys();
            user.setId(1);
            user.setUsername("admin");
            user.setUserpass("admin");
            Form<Usersys> frm = formFactory.form(Usersys.class).bindFromRequest().fill(user);
            LoginController.GenerateToken();
            return ok(login.render(frm, new HashSet<>(), token));
        } catch (Exception e) {
            return ok(_404.render());
        }
    }

    public Result authenticate() {

        if (!(session("token").toString().equals(token))) {
            return forbidden(_403.render());
        }

        Form<Usersys> frm = formFactory.form(Usersys.class).bindFromRequest();
        Set<ErrorClass> errors = Validation.validateDataLogin(frm.get());
        if (errors.isEmpty()) {
            if (this.authenticate(frm.get())) {
                session().clear();
                session("connected", frm.get().getUsername());
                flash("success", "Successfully logged.");
                return redirect(routes.HomeController.index());
            }
            flash("danger", "Invalid username and password.");
            LoginController.GenerateToken();
            return badRequest(login.render(frm, new HashSet<>(), token));
        } else {
            LoginController.GenerateToken();
            return badRequest(login.render(frm, errors, token));
        }
    }

    public Result logout() {
        session().clear();
        flash("success", "Successfully logged out.");
        return redirect(routes.HomeController.index());
    }

    private static boolean authenticate(Usersys usersys) {

        String _username = services.HashUtil.HashString(usersys.getUsername().trim());
        String _password = services.HashUtil.HashString(usersys.getUserpass().trim());
        Usersys user = Usersys.find.where().eq("username", _username).where().eq("userpass", _password).findUnique();
        if (user != null) {
            return true;
        }
        return false;
    }

    private static void GenerateToken() {
        LoginController.token = Token.generateToken();
        session().remove("token");
        session("token", LoginController.token);
    }
}
