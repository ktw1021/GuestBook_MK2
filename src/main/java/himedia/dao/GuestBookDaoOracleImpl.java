package himedia.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestBookDaoOracleImpl implements GuestBookDao {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "himedia";
    private static final String DB_PASS = "himedia";
    
    static {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC driver not found", e);
        }
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
    
    public List<GuestBookVo> getList() {
    	
        List<GuestBookVo> dataList = new ArrayList<>();
        String sql = "SELECT no, name, TO_CHAR(reg_date, 'YYYY-MM-DD HH24:MI') AS reg_date, content FROM GuestBook";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                
                Long no = rs.getLong("no");
                String name = rs.getString("name");
                String reg_date = rs.getString("reg_date");
                String content = rs.getString("content");
                
                GuestBookVo vo = new GuestBookVo(no, name, reg_date, content);
                dataList.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    
    public boolean insert(String name, String password, String content) {
        String sql = "INSERT INTO GuestBook (name, password, content) VALUES (?, ?, ?)";
        
        int insertedCount = 0;
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, content);
            insertedCount = pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 1 == insertedCount;
    }
    @Override
    public boolean deleteWithPasswordCheck(long no, String password) {
        String selectSql = "SELECT password FROM GuestBook WHERE no = ?";
        String deleteSql = "DELETE FROM GuestBook WHERE no = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            selectStmt.setLong(1, no);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    if (dbPassword.equals(password)) {
                        deleteStmt.setLong(1, no);
                        int rowsAffected = deleteStmt.executeUpdate();
                        return rowsAffected > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public Map<String, String> getEntry(int no) {
        String sql = "SELECT no, name, content FROM GuestBook WHERE no = ?";
        Map<String, String> entry = new HashMap<>();
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, no);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entry.put("no", rs.getString("no"));
                    entry.put("name", rs.getString("name"));
                    entry.put("content", rs.getString("content"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entry;
    }
    
    public boolean updateEntry(int no, String name, String password, String content) {
        String sql = "UPDATE GuestBook SET name = ?, content = ? WHERE no = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, content);
            pstmt.setInt(3, no);
            pstmt.setString(4, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
