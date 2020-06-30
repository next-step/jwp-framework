package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.db.DataBase;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static core.annotation.web.RequestMethod.GET;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = GET)
    public ModelAndView getMainPage(HttpServletRequest request) {
        request.setAttribute("users", DataBase.findAll());

        return new ModelAndView("home.jsp");
    }
}
