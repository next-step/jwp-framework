package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        logger.debug("fields : {}", Arrays.toString(clazz.getDeclaredFields()));
        logger.debug("constructors : {}", Arrays.toString(clazz.getDeclaredConstructors()));
        logger.debug("methods : {}", Arrays.toString(clazz.getDeclaredMethods()));
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

    @Test
    public void privateFieldAccess() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Field[] fields = clazz.getDeclaredFields();
        Student newStudent = clazz.getDeclaredConstructor().newInstance();
        for (Field f : fields) {
            f.setAccessible(true);

            if(f.getName().equals("name")) {
                f.set(newStudent, "재성");
            }
            if(f.getName().equals("age")) {
                f.set(newStudent, 30);
            }
        }
        logger.debug("newStudent : {}", newStudent);
        assertThat(newStudent.getName()).isEqualTo("재성");
        assertThat(newStudent.getAge()).isEqualTo(30);
    }
}
