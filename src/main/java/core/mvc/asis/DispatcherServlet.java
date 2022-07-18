package core.mvc.asis;

import core.mvc.ModelAndView;
import core.mvc.exception.NotFoundException;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import core.mvc.tobe.HandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";
    private static final String BASE_PACKAGE = "next.controller";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        LegacyHandlerMapping legacyHandlerMapping = new LegacyHandlerMapping();
        legacyHandlerMapping.initMapping();
        handlerMappings.add(legacyHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            handle(req, resp);
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

    private void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = getHandler(request);
        if (handler instanceof Controller) {
            render(
                    ((Controller) handler).execute(request, response),
                    request,
                    response
            );
        } else if (handler instanceof HandlerExecution) {
            render(
                    ((HandlerExecution) handler).handle(request, response),
                    request,
                    response
            );
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(it -> it.hasHandler(request))
                .findAny()
                .orElseThrow(() -> new NotFoundException(request))
                .getHandler(request);
    }

    private void render(
            Object view,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        if (view instanceof String) {
            String viewName = (String) view;
            move(viewName, request, response);
        } else if (view instanceof ModelAndView) {
            ModelAndView modelAndView = (ModelAndView) view;
            modelAndView.getView()
                    .render(modelAndView.getModel(), request, response);
        }
    }
}
