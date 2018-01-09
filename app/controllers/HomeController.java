package controllers;

import play.mvc.*;

import views.html.home.*;

public class HomeController extends Controller {

    public Result index() {

       return ok(index.render("Home"));
    }
}
