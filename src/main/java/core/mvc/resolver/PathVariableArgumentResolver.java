package core.mvc.resolver;

import core.annotation.web.PathVariable;
import core.annotation.web.RequestMapping;
import core.mvc.exception.InvalidPathVariableException;
import core.mvc.exception.NotPresentRequestMappingException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

public class PathVariableArgumentResolver extends AbstractAnnotationArgumentResolver {
    private static final PathPatternParser pathPatternParser = new PathPatternParser();

    @Override
    public boolean supports(MethodParameter methodParameter) {
        return supportAnnotation(methodParameter, PathVariable.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) {
        String pattern = getPattern(methodParameter.getMethod());
        String path = request.getRequestURI();
        String key = getPathVariableKey(
            getAnnotation(methodParameter, PathVariable.class),
            methodParameter.getParameterName()
        );
        return resolve(
            methodParameter,
            getArgument(pattern, path, key)
        );
    }

    private String getPattern(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            throw new NotPresentRequestMappingException(method);
        }
        return method.getAnnotation(RequestMapping.class).value();
    }

    private String getPathVariableKey(PathVariable pathVariable, String parameterName) {
        return StringUtils.isNotBlank(pathVariable.name()) ? pathVariable.name()
            : StringUtils.isNotBlank(pathVariable.value()) ? pathVariable.value()
                : parameterName;
    }

    private Object getArgument(String pattern, String path, String key) {
        PathPattern.PathMatchInfo pathMatchInfo = pathPatternParser.parse(pattern)
            .matchAndExtract(PathContainer.parsePath(path));
        if (pathMatchInfo == null) {
            throw new InvalidPathVariableException(pattern, path, key);
        }
        return pathMatchInfo.getUriVariables().get(key);
    }
}
