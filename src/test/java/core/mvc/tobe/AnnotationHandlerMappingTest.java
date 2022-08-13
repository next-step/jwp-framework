package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import core.annotation.web.RequestMethod;
import core.db.DataBase;
import core.mvc.ModelAndView;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    public void setup() {
        handlerMapping = new AnnotationHandlerMapping("core.mvc.tobe");
    }

    @Test
    void create_find() throws Exception {
        User user = new User("pobi", "password", "포비", "pobi@nextstep.camp");
        createUser(user);
        assertThat(DataBase.findUserById(user.getUserId())).isEqualTo(user);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users");
        request.setParameter("userId", user.getUserId());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutable execution = handlerMapping.getHandler(request);
        execution.handle(request, response);

        assertThat(request.getAttribute("user")).isEqualTo(user);
    }

    private void createUser(User user) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/users");
        request.setParameter("userId", user.getUserId());
        request.setParameter("password", user.getPassword());
        request.setParameter("name", user.getName());
        request.setParameter("email", user.getEmail());
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutable execution = handlerMapping.getHandler(request);
        execution.handle(request, response);
    }

    @DisplayName("모든 HTTP Method를 지원한다")
    @ParameterizedTest
    @EnumSource(RequestMethod.class)
    void all_request_method(RequestMethod method) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method.name(), "/allMethod");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutable execution = handlerMapping.getHandler(request);
        final ModelAndView mav = execution.handle(request, response);

        assertThat(mav.getObject("thank")).isEqualTo("you");

    }

    @DisplayName("PathPattenr이 일치하는 handler를 반환한다")
    @Test
    void find_handler_with_path_pattern() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/welcome/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerExecutable execution = handlerMapping.getHandler(request);
        final ModelAndView mav = execution.handle(request, response);

        assertThat(mav.getObject("path")).isEqualTo("pattern");
    }

    @DisplayName("유효한 handler가 없는 경우 NotFoundExecution을 반환한다 ")
    @Test
    void not_found_execution() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/존재하지않는주소");
        HandlerExecutable actual = handlerMapping.getHandler(request);

        assertThat(actual).isInstanceOf(NotFoundExecution.class);
    }
}
