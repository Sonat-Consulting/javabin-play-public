package controllers;

import models.Resource;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render(Resource.find.all()));
    }
  
}
