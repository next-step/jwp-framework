package next.controller;

import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.JSPView;
import core.mvc.ModelAndView;
import core.mvc.asis.Controller;
import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@core.annotation.web.Controller
public class LoginController implements Controller {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return "redirect:/";
        } else {
            req.setAttribute("loginFailed", true);
            return "/user/login.jsp";
        }
    }

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ModelAndView execute2(HttpServletRequest req, HttpServletResponse resp) {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            ModelAndView modelAndView = new ModelAndView(new JSPView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);
            return new ModelAndView(new JSPView("redirect:/"));
        } else {
            ModelAndView modelAndView = new ModelAndView(new JSPView("/user/login.jsp"));
            modelAndView.addObject("loginFailed", true);
            return modelAndView;
        }
    }
}
