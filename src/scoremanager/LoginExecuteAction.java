package scoremanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDAO;
import tool.Action;

public class LoginExecuteAction extends Action {
    public String execute(HttpServletRequest request, HttpServletResponse response)
    throws Exception {
        HttpSession session = request.getSession();
        String id = request.getParameter("id");
        String password = request.getParameter("password");

        TeacherDAO dao = new TeacherDAO();
        Teacher teacher = dao.search(id, password);

        if (teacher != null) {
            session.setAttribute("teacher", teacher);
            // redirectで遷移
            response.sendRedirect(request.getContextPath() + "/scoremanager/menu.jsp");
            return null; // nullをreturnしてforwardしないように
        } else {
            return "login-error.jsp";
        }
    }
}
