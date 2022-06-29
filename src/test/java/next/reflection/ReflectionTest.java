package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());

        logger.debug("### Field");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            logger.debug("Field - name: {}, type: {}, modifier : {}", declaredField.getName(), declaredField.getType(), declaredField.getModifiers());
        }

        logger.debug("### Constructor");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            Parameter[] parameters = constructor.getParameters();
            logger.debug("Constructor - name : {}, modifier : {}", constructor.getName(), constructor.getModifiers());
            for (Parameter parameter : parameters) {
                logger.debug("Parameter - name : {}, type : {}, modifier : {}", parameter.getName(), parameter.getType(), parameter.getModifiers());
            }
        }

        logger.debug("### Methods");
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            logger.debug("Method - name : {}, return type : {}", declaredMethod.getName(), declaredMethod.getReturnType());
            Parameter[] parameters = declaredMethod.getParameters();
            for (Parameter parameter : parameters) {
                logger.debug("Parameter - name : {}, type : {}, modifier : {}", parameter.getName(), parameter.getType(), parameter.getModifiers());
            }
        }
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
