package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        // 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(it -> logger.debug("Field : {}", it.getName()));
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(it -> logger.debug("constructor : {}", it.getName()));
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(it -> logger.debug("method : {}", it.getName()));
    }

    @Test
    public void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = new Student();
        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "이름");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 5);

        logger.debug("student : {}", student);
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
