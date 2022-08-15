package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
    public static final String DELIMITER = ",";

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
    }

    @DisplayName("Question클래스의 모든 필드, 생성자, 메소드 정보를 출력한다.")
    @Test
    public void printAllByQuestion() {
        Class<Question> questionClass = Question.class;

        String constructors = extractAndJoining(questionClass.getDeclaredConstructors(), DELIMITER);
        String methods = extractAndJoining(questionClass.getDeclaredMethods(), DELIMITER);
        String fields = extractAndJoining(questionClass.getDeclaredFields(), DELIMITER);

        logger.info("생성자 목록: {}", constructors);
        logger.info("메서드 목록: {}", methods);
        logger.info("필드 목록: {}", fields);
    }

    private static String extractAndJoining(Member[] objects, String delimiter) {
        return Arrays.stream(objects)
            .map(Member::getName)
            .collect(Collectors.joining(delimiter));
    }

}
