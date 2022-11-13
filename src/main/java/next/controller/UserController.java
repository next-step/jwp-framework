package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(User user, HttpServletResponse response) {
        logger.debug("User : {}", user);
        DataBase.addUser(user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView list(HttpSession session, HttpServletRequest request) {

        if (!UserSessionUtils.isLogined(session)) {
            return new ModelAndView("redirect:/users/loginForm");
        }

        request.setAttribute("users", DataBase.findAll());
        return new ModelAndView("/user/list.jsp");
    }
}
