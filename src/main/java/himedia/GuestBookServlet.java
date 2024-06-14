package himedia;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import himedia.dao.GuestBookDao;
import himedia.dao.GuestBookDaoOracleImpl;
import himedia.dao.GuestBookVo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/guestbook")
public class GuestBookServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private GuestBookDao dao;

    @Override
    public void init() throws ServletException {
        dao = new GuestBookDaoOracleImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("a");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listEntries(request, response);
                break;
            case "delete":
                deleteEntry(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            default:
                listEntries(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("a");

        switch (action) {
            case "add":
                addEntry(request, response);
                break;
            case "update":
                updateEntry(request, response);
                break;
            default:
                listEntries(request, response);
                break;
        }
    }

    private void listEntries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<GuestBookVo> entries = dao.getList();
        request.setAttribute("entries", entries);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void addEntry(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String content = request.getParameter("content");

        if (name != null && password != null && content != null) {
            dao.insert(name, password, content);
        }

        response.sendRedirect("guestbook?a=list");
    }

    private void deleteEntry(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String noString = request.getParameter("no");
        String password = request.getParameter("password");

        if (noString == null || password == null) {
            response.sendRedirect("guestbook?a=list&message=invalid_request");
            return;
        }

        try {
            int no = Integer.parseInt(noString);
            boolean isDeleted = dao.deleteWithPasswordCheck(no, password);

            if (isDeleted) {
                response.sendRedirect("guestbook?a=list&message=deleted");
            } else {
                response.sendRedirect("guestbook?a=list&message=wrong_password");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("guestbook?a=list&message=invalid_request");
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int no = Integer.parseInt(request.getParameter("no"));
            Map<String, String> entry = dao.getEntry(no);
            request.setAttribute("entry", entry);

            if (!response.isCommitted()) {
                request.getRequestDispatcher("/edit.jsp").forward(request, response);
            } else {
                response.getWriter().println("응답이 이미 커밋된 상태입니다.");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("guestbook?a=list&message=invalid_request");
        }
    }

    private void updateEntry(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int no = Integer.parseInt(request.getParameter("no"));
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String content = request.getParameter("content");

            boolean isUpdated = dao.updateEntry(no, name, password, content);

            if (isUpdated) {
                response.getWriter().println("<script>alert('수정되었습니다.'); location.href='guestbook?a=list';</script>");
            } else {
                response.getWriter().println("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("guestbook?a=list&message=invalid_request");
        }
    }
}
