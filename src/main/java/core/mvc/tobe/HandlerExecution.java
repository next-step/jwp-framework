package core.mvc.tobe;

import core.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private Object controller;
    private Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

//    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        return null;
//    }

    // TODO ModelAndView 반환하기.
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Object obj = method.invoke(controller, request, response);

//        return (String) obj;

        return (ModelAndView) method.invoke(controller, request, response);
    }
}
