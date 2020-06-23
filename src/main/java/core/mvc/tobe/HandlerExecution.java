package core.mvc.tobe;

import core.mvc.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

public class HandlerExecution {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

        String[] valueNames = nameDiscoverer.getParameterNames(method);


        // 이게 정의된 객체라는걸 어떻게 확인하지....?

        //요청 들어온 파라미터 이름
        Object[] parameters = Stream.of(valueNames)
                .map(request::getParameter)
                .toArray();

        // 메소드 파라미터 타입
        Class<?>[] parameterTypes = method.getParameterTypes();

        for (int i = 0; i < parameterTypes.length; i++) {
            Object parameter = parameters[i];
            if (parameter.getClass().equals(parameterTypes[i])) {
                continue;
            }

            if(!ClassUtils.isPrimitiveOrWrapper(parameterTypes[i])) {
                // 객체 만들어야함!!
                Class bean = Class.forName(parameterTypes.getClass().getName());

                Field[] field = bean.getDeclaredFields();

                for(int j=0; j<field.length; j++) {
                    String value = request.getParameter(field[i].getName());
                    if(value != null) {
                        Object beanValue = this.castPrimitiveType(field[i].getType(), value);

                    }
                }
            }
            if (parameterTypes[i].isPrimitive()) {
                parameters[i] = castPrimitiveType(parameterTypes[i], parameter.toString());
            }

        }

        return (ModelAndView) method.invoke(clazz.newInstance(), parameters);
    }

    private Object castPrimitiveType(Class<?> clazz, String value) {
        if (clazz.equals(int.class)) {
            return Integer.parseInt(value);
        }

        if (clazz.equals(byte.class)) {
            return Byte.parseByte(value);
        }

        if (clazz.equals(short.class)) {
            return Short.parseShort(value);
        }

        if (clazz.equals(long.class)) {
            return Long.parseLong(value);
        }

        if (clazz.equals(float.class)) {
            return Float.parseFloat(value);
        }

        if (clazz.equals(double.class)) {
            return Double.parseDouble(value);
        }

        if (clazz.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        }

        if (clazz.equals(char.class)) {
            return value;
        }

        return value;
    }

}
