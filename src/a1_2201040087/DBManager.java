package a1_2201040087;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String JDBC_URL = "jdbc:sqlite:src/a1_2201040087/SCManager.db";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(JDBC_URL);
            return conn;
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối database: " + e.getMessage());
            return null;
        }
    }

    /**
     * Tạo các bảng cần thiết nếu chưa tồn tại.
     */
    public static void setupDatabase() {
        String sqlStudents = "CREATE TABLE IF NOT EXISTS Students ("
                + " id INTEGER PRIMARY KEY, "
                + " name TEXT NOT NULL);";

        String sqlCourses = "CREATE TABLE IF NOT EXISTS Courses ("
                + " id TEXT PRIMARY KEY, "
                + " name TEXT NOT NULL);";

        String sqlScores = "CREATE TABLE IF NOT EXISTS Scores ("
                + " id INTEGER PRIMARY KEY, "
                + " student_id INTEGER NOT NULL, "
                + " course_id TEXT NOT NULL, "
                + " score REAL NOT NULL, "
                + " FOREIGN KEY (student_id) REFERENCES Students(id), "
                + " FOREIGN KEY (course_id) REFERENCES Courses(id));";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(sqlStudents);
            stmt.execute(sqlCourses);
            stmt.execute(sqlScores);
            System.out.println("Cơ sở dữ liệu đã được khởi tạo.");

            // Khởi tạo dữ liệu mẫu nếu chưa có
            if (getAllStudents().isEmpty()) {
                initializeData();
            }

        } catch (SQLException e) {
            System.err.println("Lỗi tạo bảng: " + e.getMessage());
        }
    }

    /**
     * Nạp dữ liệu mẫu vào các bảng.
     */
    public static void initializeData() {
        String insertStudents = "INSERT INTO Students (id, name) VALUES " +
                "(2201040210, 'John'), " +
                "(2201040011, 'David'), " +
                "(2201040005, 'Alex'), " +
                "(2201040100, 'Mary');";
        String insertCourses = "INSERT INTO Courses (id, name) VALUES " +
                "('61FIT3JSD', 'Java Programming'), " +
                "('61FIT2DBS', 'Database System');";
        String insertScores = "INSERT INTO Scores (student_id, course_id, score) VALUES " +
                "('2201040210', '61FIT3JSD', 8.5), " +
                "('2201040005', '61FIT3JSD', 6.5), " +
                "('2201040100', '61FIT2DBS', 8), " +
                "('2201040100', '61FIT3JSD', 9), " +
                "('2201040011', '61FIT2DBS', 7.0);";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.executeUpdate(insertStudents);
            stmt.executeUpdate(insertCourses);
            stmt.executeUpdate(insertScores);
            System.out.println("Đã nạp dữ liệu mẫu.");
        } catch (SQLException e) {
            // Lỗi này thường do dữ liệu đã tồn tại (DUPLICATE PRIMARY KEY)
            System.out.println("Lưu ý: Không thể nạp dữ liệu mẫu vì có thể đã tồn tại.");
        }
    }


    // --- HÀM TẢI DỮ LIỆU ---

    /**
     * Lấy tất cả Students.
     * @return List<String[]> chứa dữ liệu từng hàng (ID, Name, "delete")
     */
    public static List<String[]> getAllStudents() {
        List<String[]> data = new ArrayList<>();
        String sql = "SELECT id, name FROM Students ORDER BY id ASC";

        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Chúng ta sẽ lấy ID và Name
                data.add(new String[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        "Delete" // Giá trị hiển thị cho nút
                });
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return data;
    }

    /**
     * Lấy tất cả Courses.
     */
    public static List<String[]> getAllCourses() {
        List<String[]> data = new ArrayList<>();
        String sql = "SELECT id, name FROM Courses ORDER BY id ASC";

        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                data.add(new String[]{
                        rs.getString("id"),
                        rs.getString("name"),
                        "Delete"
                });
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return data;
    }

    /**
     * Lấy tất cả Scores.
     */
    public static List<String[]> getAllScores() {
        List<String[]> data = new ArrayList<>();
        String sql = "SELECT s.id, s.student_id, s.course_id, s.score FROM Scores s ORDER BY s.id ASC";

        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                data.add(new String[]{
                        rs.getString("id"),          // Cột 0: PK của Scores (Dùng để Delete)
                        rs.getString("student_id"),  // Cột 1
                        rs.getString("course_id"),   // Cột 2
                        rs.getString("score"),       // Cột 3
                        "Delete"                     // Cột 4: Nút
                });
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return data;
    }

    // --- HÀM XÓA DỮ LIỆU ---

    /**
     * Xóa một bản ghi theo tên bảng và ID chính.
     */
    public static void deleteRecord(String tableName, String recordId) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");  // quan trọng: phải chạy trước các lệnh khác
            stmt.close();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // KIỂM TRA TÊN BẢNG ĐỂ DÙNG setString HOẶC setInt
            if (tableName.equalsIgnoreCase("Courses")) {
                // Courses có ID là TEXT (ví dụ: '61FIT3JSD')
                pstmt.setString(1, recordId);
            } else {
                // Students và Scores có ID là INTEGER/AUTOINCREMENT
                try {
                    long id = Long.parseLong(recordId.trim());
                    pstmt.setLong(1, id);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi chuyển đổi ID: ID " + recordId + " không phải là số nguyên.");
                    return;
                }
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa bản ghi " + recordId + " từ bảng " + tableName);
            } else {
                System.out.println("Không tìm thấy bản ghi " + recordId + " để xóa.");
            }

        } catch (SQLException e) {
            System.err.println("Lỗi xóa bản ghi: " + e.getMessage());
        }
    }
}