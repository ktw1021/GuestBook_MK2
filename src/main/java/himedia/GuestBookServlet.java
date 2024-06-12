package himedia;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        dao = new GuestBookDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	GuestBookDao dao = new GuestBookDao();
    	List<String[]> entries = dao.getList();
    	
    	request.setAttribute("entries", entries);
    	
    	String action = request.getParameter("action");
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
        String action = request.getParameter("action");

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
        List<String[]> entries = dao.getList();
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

        response.sendRedirect("guestbook?action=list");
    }

    private void deleteEntry(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String noString = request.getParameter("no");
        String password = request.getParameter("password");

        if (noString == null || password == null) {
            response.sendRedirect("guestbook?action=list&message=invalid_request");
            return;
        }

        int no = Integer.parseInt(noString);

        try {
            boolean isDeleted = dao.deleteWithPasswordCheck(no, password);
            
            if (isDeleted) {
                response.sendRedirect("guestbook?action=list&message=deleted");
            } else {
                response.sendRedirect("guestbook?action=list&message=wrong_password");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("guestbook?action=list&message=invalid_request");
        } 
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int no = Integer.parseInt(request.getParameter("no"));
        Map<String, String> entry = dao.getEntry(no);
        request.setAttribute("entry", entry);
        
        // 응답이 커밋되지 않은 경우에만 forward를 시도
        if (!response.isCommitted()) {
            request.getRequestDispatcher("/edit.jsp").forward(request, response);
        } else {
            // 응답이 이미 커밋된 경우에 대한 처리
            // 예를 들어, 로깅하거나 다른 처리를 수행할 수 있음
            // 또는 클라이언트에게 에러 메시지를 전달할 수도 있음
            response.getWriter().println("응답이 이미 커밋된 상태입니다.");
        }
    }


    private void updateEntry(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int no = Integer.parseInt(request.getParameter("no"));
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String content = request.getParameter("content");

        boolean isUpdated = dao.updateEntry(no, name, password, content);

        if (isUpdated) {
            response.getWriter().println("<script>alert('수정되었습니다.'); location.href='guestbook?action=list';</script>");
        } else {
            response.getWriter().println("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
        }
    }
}
