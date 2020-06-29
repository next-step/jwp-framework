package core.mvc.tobe;

import core.mvc.ModelAndView;
import core.mvc.ViewFactory;
import core.mvc.asis.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHandlerAdapter implements HandlerCommand {

    private Controller controller;

    public RequestHandlerAdapter(Controller controller) {
        this.controller = controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (controller == null) {
            return null;
        }

        String viewName = controller.execute(request, response);

        return new ModelAndView(ViewFactory.create(viewName));
    }
}
