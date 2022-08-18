package core.mvc.tobe;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller(value = "/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static final String NOT_OWNER_USER = "다른 사용자의 정보를 수정할 수 없습니다.";
    private static final String NOT_FOUND_USER = "사용자를 찾을 수 없습니다.";

    @RequestMapping
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        if (!UserSessionUtils.isLogined(req.getSession())) {

            return new ModelAndView(new JspView("redirect:/users/loginForm"));
        }

        req.setAttribute("users", DataBase.findAll());

        return new ModelAndView(new JspView("/user/list.jsp"));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        String password = req.getParameter("password");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            req.setAttribute("loginFailed", true);

            return new ModelAndView(new JspView("/user/login.jsp"));
        }
        if (user.matchPassword(password)) {
            HttpSession session = req.getSession();
            session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

            return new ModelAndView(new JspView("redirect:/"));
        } else {
            req.setAttribute("loginFailed", true);

            return new ModelAndView(new JspView("/user/login.jsp"));
        }
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/form")
    public ModelAndView form(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        return new ModelAndView(new JspView("/user/form.jsp"));
    }

    @RequestMapping(value = "/loginForm")
    public ModelAndView loginForm(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {
        return new ModelAndView(new JspView("/user/login.jsp"));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = new User(req.getParameter("userId"), req.getParameter("password"),
            req.getParameter("name"),
            req.getParameter("email"));
        log.debug("User : {}", user);

        DataBase.addUser(user);

        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/updateForm")
    public ModelAndView updateForm(HttpServletRequest req, HttpServletResponse resp)
        throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException(NOT_OWNER_USER);
        }
        req.setAttribute("user", user);

        return new ModelAndView(new JspView("/user/updateForm.jsp"));
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView update(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = DataBase.findUserById(req.getParameter("userId"));
        if (!UserSessionUtils.isSameUser(req.getSession(), user)) {
            throw new IllegalStateException(NOT_OWNER_USER);
        }

        User updateUser = new User(req.getParameter("userId"), req.getParameter("password"),
            req.getParameter("name"),
            req.getParameter("email"));
        log.debug("Update User : {}", updateUser);
        user.update(updateUser);

        return new ModelAndView(new JspView("redirect:/"));
    }

    @RequestMapping(value = "/profile")
    public ModelAndView profile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userId = req.getParameter("userId");
        User user = DataBase.findUserById(userId);
        if (user == null) {
            throw new NullPointerException(NOT_FOUND_USER);
        }
        req.setAttribute("user", user);

        return new ModelAndView(new JspView("/user/profile.jsp"));
    }
}
