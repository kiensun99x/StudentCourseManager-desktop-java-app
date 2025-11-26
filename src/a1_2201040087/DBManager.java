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
                + " id TEXT PRIMARY KEY, "
                + " name TEXT NOT NULL);";

        String sqlCourses = "CREATE TABLE IF NOT EXISTS Courses ("
                + " id TEXT PRIMARY KEY, "
                + " name TEXT NOT NULL);";

        String sqlScores = "CREATE TABLE IF NOT EXISTS Scores ("
                + " id INTEGER PRIMARY KEY, "
                + " student_id TEXT NOT NULL, "
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
                "('2201040210', 'John'), " +
                "('2201040011', 'David'), " +
                "('2201040005', 'Alex'), " +
                "('2201040100', 'Mary');";
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

    public static void addStudent(long id, String name) throws SQLException {
        String sql = "INSERT INTO Students (id, name) VALUES (?, ?)";


        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON;");
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setLong(1, id);
        pstmt.setString(2, name);
        pstmt.executeUpdate();

    }

    /**
     * Thêm một môn học mới vào bảng Courses.
     */
    public static void addCourse(String id, String name) throws SQLException {
        // Lưu ý: Course ID là TEXT, không phải INTEGER
        String sql = "INSERT INTO Courses (id, name) VALUES (?, ?)";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON;");
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, id);
        pstmt.setString(2, name);
        pstmt.executeUpdate();

    }

    public static void addScore(String studentId, String courseId, double score) throws SQLException {
        // Lưu ý: Score ID là khóa chính tự tăng (không cần truyền vào)
        String sql = "INSERT INTO Scores (student_id, course_id, score) VALUES (?, ?, ?)";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON;");
        stmt.close();

        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Dùng setString cho studentId (long) và courseId (text) vì cột khóa ngoại là TEXT/INTEGER linh hoạt trong SQLite
        pstmt.setString(1, studentId);
        pstmt.setString(2, courseId);
        pstmt.setDouble(3, score);
        pstmt.executeUpdate();
    }

    // --- HÀM XÓA DỮ LIỆU ---

    /**
     * Xóa một bản ghi theo tên bảng và ID chính.
     */
    public static String deleteRecord(String tableName, String recordId) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.close();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // KIỂM TRA TÊN BẢNG ĐỂ DÙNG setString HOẶC setInt
            if (!tableName.equalsIgnoreCase("Scores")) {
                // Courses có ID là TEXT (ví dụ: '61FIT3JSD')
                pstmt.setString(1, recordId);
            } else {
                // Students và Scores có ID là INTEGER/AUTOINCREMENT
                try {
                    long id = Long.parseLong(recordId.trim());
                    pstmt.setLong(1, id);
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi chuyển đổi ID: ID " + recordId + " không phải là số nguyên.");
                    return "Invalid ID format";
                }
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đã xóa bản ghi " + recordId + " từ bảng " + tableName);
                return null;
            } else {
                System.out.println("Không tìm thấy bản ghi " + recordId + " để xóa.");
                return "Record not found";
            }

        } catch (SQLException e) {
            System.err.println("Lỗi xóa bản ghi: " + e.getMessage());
            if (e.getMessage().contains("FOREIGN KEY")) {
                return "FOREIGN_KEY_ERROR";
            }
            return e.getMessage();
        }
    }
}