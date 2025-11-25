
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void connect() {
        // Lấy đường dẫn gốc của project
//        String projectRoot = System.getProperty("user.dir");

        // Tạo đường dẫn tương đối đến file
//        String url = "jdbc:sqlite:" + projectRoot + "/src/a1_2201040087/StudentCourseManager.db";
        String url = "jdbc:sqlite:StudentCourseManager.db";

        try (var conn = DriverManager.getConnection(url)) {
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        connect();
    }
}