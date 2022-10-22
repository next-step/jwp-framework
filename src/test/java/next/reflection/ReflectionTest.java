package next.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Question 클래스의 모든 필드, 생성자, 메서드에 대한 정보를 출력한다.")
    public void showClass() {
        Class<Question> clazz = Question.class;

        logger.debug("Class Name: {}", clazz.getName());
        logger.debug("All Fields: {}", Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("All Constructors: {}", Arrays.toString(clazz.getConstructors()));
        logger.debug("All Methods: {}", Arrays.toString(clazz.getDeclaredMethods()));

        assertThat(clazz.getDeclaredFields()).hasSize(6);
        assertThat(clazz.getConstructors()).hasSize(2);
        assertThat(clazz.getDeclaredMethods()).hasSize(11);
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
