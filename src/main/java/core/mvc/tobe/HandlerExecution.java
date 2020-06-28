package core.mvc.tobe;

import core.mvc.Handler;
import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface HandlerExecution extends Handler {
    ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
