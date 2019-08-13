package core.mvc.asis.view;

import core.mvc.View;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author : yusik
 * @date : 2019-08-14
 */
public class LegacyView implements View {

    private String viewName;

    public LegacyView(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(viewName);
        System.out.println(rd);
        System.out.println(viewName);
        rd.forward(request, response);
    }
}
