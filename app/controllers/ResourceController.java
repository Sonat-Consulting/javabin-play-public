package controllers;

import models.Resource;
import play.data.Form;
import play.mvc.Result;
import views.html.index;

import static play.data.Form.*;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.redirect;

/**
 * @author sondre
 */
public class ResourceController {

    public static Result save() {
        final Form<Resource> resourceForm = form(Resource.class).bindFromRequest();
        if(resourceForm.hasErrors()) {
            return badRequest(index.render(Resource.find.all(), resourceForm));
        } else {
            resourceForm.get().save();
            return redirect("/");
        }

    }
}
