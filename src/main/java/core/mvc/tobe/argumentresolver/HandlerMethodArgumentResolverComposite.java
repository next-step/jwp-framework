package core.mvc.tobe.argumentresolver;

import core.mvc.tobe.MethodParameter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerMethodArgumentResolverComposite {

    private static final List<HandlerMethodArgumentResolver> RESOLVER_LIST = new ArrayList<>();
    private static final DefaultMethodArgumentResolver DEFAULT_RESOLVER = new DefaultMethodArgumentResolver();
    static {
        RESOLVER_LIST.add(new UserMethodArgumentResolver());
        RESOLVER_LIST.add(new PathVariableMethodArgumentResolver());
    }

    public static Object[] resolveParameters(Method method, HttpServletRequest request, HttpServletResponse response) {
        Parameter[] parameters = method.getParameters();
        final int length = parameters.length;
        Object[] values = new Object[length];

        for (int i = 0; i < length; ++i) {
            MethodParameter methodParameter = new MethodParameter(method, i, parameters[i].getAnnotations());

            values[i] = RESOLVER_LIST.stream()
                .filter(r -> r.supportsParameter(methodParameter))
                .findFirst()
                .orElse(DEFAULT_RESOLVER)
                .resolveArgument(methodParameter, request, response);
        }

        return values;
    }

}
