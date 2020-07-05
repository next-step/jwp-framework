package core.mvc.tobe;

import core.mvc.Handler;
import core.mvc.ModelAndView;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution implements Handler {

    private Method method;
    private Object instance;
    private ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    public HandlerExecution(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object[] args = Arrays.stream(nameDiscoverer.getParameterNames(method))
                .map(request::getParameter)
                .toArray();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < method.getParameterCount(); i++) {
            if (HttpSession.class.equals(parameterTypes[i])) {
                args[i] = request.getSession();
            }
        }
        return (ModelAndView) method.invoke(instance, args);
    }
}
