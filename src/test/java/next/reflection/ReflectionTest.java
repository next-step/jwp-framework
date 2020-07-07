package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        Stream.of(clazz.getDeclaredFields())
                .map(field -> "[Field] Modifiers: " + field.getModifiers()
                        + ", Name: " + field.getName())
                .forEach(logger::info);

        Stream.of(clazz.getDeclaredMethods())
                .map(method -> {
                    String parameterTypes = of(method.getParameterTypes())
                            .map(Class::getTypeName)
                            .collect(joining(","));
                    return "[Method] Modifiers: " + method.getModifiers()
                            + ", Name: " + method.getName()
                            + ", ParameterType: " + parameterTypes;
                }).forEach(logger::info);


        Stream.of(clazz.getDeclaredConstructors())
                .map(constructor -> {
                    String parameterTypes = of(constructor.getParameterTypes())
                            .map(Class::getTypeName)
                            .collect(joining(","));

                    return "[Constructor] Modifiers: " + constructor.getModifiers()
                            + ", Name: " + constructor.getName()
                            + ", ParameterType: " + parameterTypes;
                }).forEach(logger::info);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }
}
