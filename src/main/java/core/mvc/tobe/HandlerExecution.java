package core.mvc.tobe;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import core.mvc.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;
    private final ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final ArgumentResolvers argumentResolvers = new ArgumentResolvers();

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        MethodParameter[] methodParameters = getMethodParameters();
        Object[] arguments = new Object[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            arguments[i] = resolveArgument(methodParameters[i], request, response);
        }
        return (ModelAndView) method.invoke(handler, arguments);
    }

    private MethodParameter[] getMethodParameters() {
        List<Parameter> parameters = Stream.of(method.getParameters()).collect(Collectors.toList());
        return IntStream.range(0, parameters.size())
            .mapToObj(index -> new MethodParameter(parameters.get(index), method, index))
            .toArray(MethodParameter[]::new);
    }

    private Object resolveArgument(MethodParameter parameter, HttpServletRequest request, HttpServletResponse response) {
        return argumentResolvers.resolve(parameter, request, response);
    }
}
