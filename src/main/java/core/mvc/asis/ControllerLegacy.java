package core.mvc.asis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ControllerLegacy {
    String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}