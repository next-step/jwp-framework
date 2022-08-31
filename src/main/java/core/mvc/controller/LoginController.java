package core.mvc.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JspView;
import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import next.controller.UserSessionUtils;
import next.model.User;

@Controller
public class LoginController {

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);

            return modelAndView;
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

            return new ModelAndView(new JspView("redirect:/"));
        } else {
            ModelAndView modelAndView = new ModelAndView(new JspView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);

            return modelAndView;
        }
    }

    @RequestMapping(value = "/users/loginForm")
    public ModelAndView loginForm() throws Exception {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }

}
