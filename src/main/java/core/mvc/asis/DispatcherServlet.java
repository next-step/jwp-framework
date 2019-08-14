package core.mvc.asis;

import com.google.common.collect.Lists;
import core.mvc.HandlerAdapter;
import core.mvc.HandlerMapping;
import core.mvc.ModelAndView;
import core.mvc.View;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.RequestMappingHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings = Lists.newArrayList();
    private List<HandlerAdapter> handlerAdapters = Lists.newArrayList();

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Object handler = getHandler(request);

            HandlerAdapter ha = getHandlerAdapter(handler);

            ModelAndView mv = ha.handle(request, response, handler);
            View view = mv.getView();
            view.render(mv.getModel(), request, response);

        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) throws ServletException {
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new ServletException("not found handler");
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        for (HandlerAdapter handlerAdapter : this.handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new ServletException("not found handler adapter");
    }

    private void initHandlerMappings() {
        handlerMappings.add(new RequestMapping());
        handlerMappings.add(new AnnotationHandlerMapping("next.controller"));

        // initialize
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new LegacyHandlerAdapter());
        handlerAdapters.add(new RequestMappingHandlerAdapter());
    }
}
