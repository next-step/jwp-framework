package core.mvc.asis;

import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

    private RequestMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    @Override
    public void init() throws ServletException {
        manualHandlerMapping = new RequestMapping();
        manualHandlerMapping.initMapping();

        annotationHandlerMapping = new AnnotationHandlerMapping("next.controller");
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

        Controller controller = manualHandlerMapping.findController(requestUri);
        try {
            if (controller != null) {
                move(controller.execute(request, response), request, response);

            } else {
                HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(request);
                move(handlerExecution.handle(request, response), request, response);
            }
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
