package controllers;

import play.mvc.*;

import play.data.*;

import javax.inject.Inject;

import services.ErrorClass;

import services.Token;
import services.Validation;

import views.html.error._403;
import views.html.login.*;

import java.security.MessageDigest;
import java.util.HashSet;

import java.util.Set;

import models.*;

public class LoginController extends Controller {

    private static String token = null;

    public static class Function {
        public static boolean CheckSession () {
            String user = session ("connected");
            if (user != null) {
                return true;
            } else {
                return false;
            }
        }

        public static String getUsername(){
            return session ("connected");
        }
    }

    @Inject
    FormFactory formFactory;

    public Result login () {
        User user = new User();
        user.setId(1);
        user.setUsername("Admin");
        user.setPassword("Admin121181");
        Form<User> frm = formFactory.form (User.class).bindFromRequest ().fill(user);
        LoginController.GenerateToken();
        return ok (login.render (frm, new HashSet<> (), token));
    }

    public Result authenticate () {

        if (!(session("token").toString().equals(token))) {
            return forbidden(_403.render());
        }

        Form<User> frm = formFactory.form (User.class).bindFromRequest ();
        Set<ErrorClass> errors = Validation.ValidateDataLogin (frm.get ().getUsername (), frm.get ().getPassword ());
        if (errors.isEmpty ()) {
            if (this.Authenticate (frm.get ().getUsername ().trim(), frm.get ().getPassword ().trim())) {
                session ().clear ();
                session ("connected", frm.get ().getUsername ());
                flash ("success", "Successfully logged.");
                return redirect (routes.HomeController.index ());
            }
            flash ("danger", "Invalid username and password.");
            LoginController.GenerateToken();
            return badRequest (login.render (frm, new HashSet<> (), token));
        } else {
            LoginController.GenerateToken();
            return badRequest (login.render (frm, errors, token));
        }
    }

    public Result logout () {
        session ().clear ();
        flash ("success", "You've been logged out.");
        return redirect (routes.HomeController.index ());
    }

    private static boolean Authenticate (String username, String password) {

        String _username = LoginController.HashString(username);
        String _password = LoginController.HashString(password);
        User user = User.find.where().eq("username", _username).where().eq("password", _password).findUnique();
        if(user != null)
        {
            return true;
        }
        return false;
    }

    private static String HashString(String p)
    {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(p.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }catch(Exception e){
            return null;
        }
    }

    private static void GenerateToken(){
        LoginController.token = Token.generateToken();
        session().remove("token");
        session("token", LoginController.token);
    }
}
