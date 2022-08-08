package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Sets;
import core.annotation.Repository;
import core.annotation.Service;
import core.annotation.web.Controller;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private static final String DEPTH_1 = "\t\t";
    private static final String DEPTH_2 = "\t\t\t\t";
    private static final String DEPTH_3 = "\t\t\t\t\t\t";
    private static final long TEST_QUESTION_ID = 123L;
    private static final String TEST_WRITER = "testWriter";
    private static final String TEST_TITLE = "testTitle";
    private static final String TEST_CONTENTS = "testContents";
    private static final Date TEST_CREATED_DATE = Date.from(Instant.now());
    private static final int TEST_COUNT_OF_COMMENT = 99;

    private Reflections reflections;

    @DisplayName("요구사항 6, 컴포넌트 스캔, @Controller/@Service/@Repository annotation 설정 클래스 출력")
    @Test
    void componentScanTest() {
//        this.reflections = new Reflections("core.di.factory");
        this.reflections = new Reflections(new ConfigurationBuilder()
            .forPackage("core")
            .filterInputsBy(new FilterBuilder().includePackage("core.di.factory.example"))
            .setScanners(Scanners.TypesAnnotated)
            .setParallel(true));

        Set<Class<?>> classes = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);

        for (Class<?> clazz : classes) {
            printClass(clazz);
        }

        assertThat(classes).hasSize(4);
    }

    private Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(this.reflections.getTypesAnnotatedWith(annotation));
        }
        return beans;
    }

    private void printClass(Class<?> clazz) {
        logger.debug(DEPTH_1 + clazz.getName());

        Annotation[] annotations = clazz.getAnnotations();
        logger.debug(DEPTH_2 + "Annotations: ");
        for (Annotation annotation : annotations) {
            logger.debug(DEPTH_3 + annotation);
        }

        Method[] methods = clazz.getMethods();
        logger.debug(DEPTH_2 + "Methods: ");
        for (Method method : methods) {
            logger.debug(DEPTH_3 + method.getName());
        }
    }

    @DisplayName("요구사항 5, 인자를 가진 생성자의 인스턴스 생성")
    @Test
    void createConstructorWithArgsTest() throws Exception {
        Class<Question> clazz = Question.class;
        Question question;
        Constructor<Question>[] constructors = (Constructor<Question>[]) clazz.getConstructors();
        for (Constructor<Question> constructor : constructors) {
            if (isConstructorOf(constructor, TEST_WRITER, TEST_TITLE, TEST_CONTENTS)) {
                question = constructor.newInstance(TEST_WRITER, TEST_TITLE, TEST_CONTENTS);
                logger.debug("Constructor paramCount3: " + question);
            }

            if (isConstructorOf(constructor, TEST_QUESTION_ID, TEST_WRITER, TEST_TITLE, TEST_CONTENTS, TEST_CREATED_DATE, TEST_COUNT_OF_COMMENT)) {
                question = constructor.newInstance(TEST_QUESTION_ID, TEST_WRITER, TEST_TITLE, TEST_CONTENTS, TEST_CREATED_DATE, TEST_COUNT_OF_COMMENT);
                logger.debug("Constructor paramCount6: " + question);
            }
        }
    }

    private boolean isConstructorOf(Constructor constructor, Object... objects) {
        Class[] parameterTypes = constructor.getParameterTypes();
        if (parameterTypes.length != objects.length) {
            return false;
        }

        for (int i = 0; i < parameterTypes.length; ++i) {
            if (!parameterTypes[i].isInstance(objects[i])) {
                return false;
            }
        }

        return true;
    }

    @DisplayName("요구사항 4, private field 값 할당")
    @Test
    void privateFieldAccess() throws Exception {
        Class<Student> clazz = Student.class;
        logger.debug(clazz.getName());

        Student student = clazz.getDeclaredConstructor().newInstance();

        printBefore(student);
        assertThat(student.getName()).isNull();
        assertThat(student.getAge()).isZero();

        Field nameField = clazz.getDeclaredField("name");
        Field ageField = clazz.getDeclaredField("age");
        nameField.setAccessible(true);
        ageField.setAccessible(true);
        nameField.set(student, "수정된이름");
        ageField.set(student, 99);

        printAfter(student);
        assertThat(student.getName()).isEqualTo("수정된이름");
        assertThat(student.getAge()).isEqualTo(99);
    }

    private void printBefore(Student student) {
        logger.debug(" ======= Before ");
        logger.debug(student.toString());
    }

    private void printAfter(Student student) {
        logger.debug(" ======= After ");
        logger.debug(student.toString());
    }

    @DisplayName("요구사항 1, 클래스 정보 출력")
    @Test
    void showAboutClass() {
        Class<Question> clazz = Question.class;

        printFields(clazz);
        printConstructors(clazz);
        printMethods(clazz);
    }

    private void printFields(Class<Question> clazz) {
        logger.debug(" ========= Field ========= ");
        logger.debug(" DeclaredFields: ");
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            logger.debug(DEPTH_1 + declaredField);
            Annotation[] declaredAnnotations = declaredField.getDeclaredAnnotations();
            logger.debug(DEPTH_2 + " Field Annotations: ");
            for (Annotation declaredAnnotation : declaredAnnotations) {
                logger.debug(DEPTH_2 + declaredAnnotation);
            }
        }
        logger.debug("");
    }

    private void printMethods(Class<Question> clazz) {
        logger.debug(" ========= Method (Only declared) ========= ");
        Arrays.stream(clazz.getDeclaredMethods())
            .forEach(method -> {
                logger.debug("method: ");
                logger.debug(DEPTH_1 + method.getName());

                logger.debug(DEPTH_2 + "parameterTypes: ");
                Class<?>[] parameterTypes = method.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    logger.debug(DEPTH_3 + parameterType);
                }

                logger.debug(DEPTH_2 + "annotations: ");
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    logger.debug(DEPTH_3 + declaredAnnotation);
                }
            });
    }

    private void printConstructors(Class<Question> clazz) {
        logger.debug(" ========= Constructor ========= ");
        Arrays.stream(clazz.getConstructors())
            .forEach(constructor -> {
                logger.debug("constructor: ");
                logger.debug(DEPTH_1 + constructor.getName());

                logger.debug(DEPTH_2 + "parameterTypes: ");
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (Class<?> parameterType : parameterTypes) {
                    logger.debug(DEPTH_3 + parameterType + ", ");
                }

                logger.debug(DEPTH_2 + "annotations: ");
                Annotation[] declaredAnnotations = constructor.getDeclaredAnnotations();
                for (Annotation declaredAnnotation : declaredAnnotations) {
                    logger.debug(DEPTH_2 + declaredAnnotation);
                }
            });
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
