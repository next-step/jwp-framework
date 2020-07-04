package core.mvc.tobe.resolver.argument;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {

    private Method method;

    private String parameterName;

    private Class<?> parameterType;

    private Parameter parameter;

    public MethodParameter(Method method, String parameterName, Parameter parameter) {
        this.method = method;
        this.parameterName = parameterName;
        this.parameter = parameter;
        this.parameterType = parameter.getType();
    }

    public boolean isRequestParameterArgument() {
        return parameterType.isPrimitive() || parameterType == String.class;
    }

    public boolean isJavaBeanArgument() {
        return parameterType.getConstructors().length > 0 && parameterType != String.class;
    }

    public boolean isPathVariableArgument() {
        return parameter.isAnnotationPresent(PathVariable.class);
    }

    public Object getParameterTypeValue(String parameterValue) {
        Object value = parameterValue;
        if (parameterType.equals(int.class)) {
            value = Integer.parseInt(parameterValue);
        }
        if (parameterType.equals(long.class)) {
            value = Long.parseLong(parameterValue);
        }

        return value;
    }

    public String getParameterName() {
        return parameterName;
    }

    public Class<?> getParameterType() {
        return parameterType;
    }

    public String getMethodRequestMappingValue() {
        return method.getAnnotation(RequestMapping.class).value();
    }
}
