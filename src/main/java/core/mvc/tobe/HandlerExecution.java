package core.mvc.tobe;

import core.mvc.ModelAndView;
import java.lang.reflect.Method;
import java.util.Objects;

public class HandlerExecution implements HandlerExecutable {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    @Override
    public ModelAndView handle(Object... arguments) throws Exception {
        return (ModelAndView) method.invoke(handler, arguments);
    }

    @Override
    public boolean executable() {
        return handler != null && method != null;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HandlerExecution that = (HandlerExecution) o;
        return Objects.equals(handler, that.handler) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handler, method);
    }

    @Override
    public String toString() {
        return "HandlerExecution{" +
            "handler=" + handler.getClass() +
            ", method=" + method.getName() +
            '}';
    }
}
