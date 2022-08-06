package core.mvc.tobe;

import core.mvc.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExecutable {

    boolean executable();

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
