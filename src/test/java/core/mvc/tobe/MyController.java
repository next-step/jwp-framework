package core.mvc.tobe;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.view.JspView;
import core.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MyController {
    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping("/users/findUserId")
    public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("findUserId");
        return new ModelAndView(new JspView("/users/show.jsp"));
    }

    @RequestMapping("/users")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("users findUserId");
        return new ModelAndView(new JspView("/users/list.jsp"));
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("save");
        return new ModelAndView(new JspView("redirect:/users"));
    }

    @RequestMapping(value = "/all")
    public ModelAndView nothing(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("no http methods mapping");
        return new ModelAndView(new JspView("/nothing"));
    }
}
