package core.mvc.tobe;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class AnnotationParameterHelper implements HandlerMethodHelper {
    @Override
    public boolean support(ParameterInfo parameterInfo) {
        return parameterInfo.isAnnotated();
    }

    @Override
    public Object bindingProcess(ParameterInfo parameterInfo, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<String, String> values = parameterInfo.getUrlVariables(request);

        String value = values.get(parameterInfo.getValueName());
        return parameterInfo.resolveRequestValue(value);
    }
}
