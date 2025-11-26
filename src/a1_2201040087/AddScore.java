package a1_2201040087;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class AddScore extends WindowAdapter implements ActionListener {
    private JFrame win;
    private JFrame parentGUI;
    private JComboBox<String> cbStudentId;
    private JComboBox<String> cbCourseId;
    private JTextField txtScore;
    private JButton btnAdd;
    private MainApp mainApp;

    private List<String> studentIds;
    private List<String> courseIds;

    public AddScore(JFrame parent, MainApp mainApp){
        this.parentGUI = parent;
        this.mainApp = mainApp;
        createGUI();
    }

    private void createGUI(){
        win = new JFrame("Add New Score");
        win.setLayout(new BorderLayout(10, 10));
        win.setSize(400, 250);
        win.setLocationRelativeTo(parentGUI);

        // --- Lấy dữ liệu  ---
        List<String[]> students = DBManager.getAllStudents();
        List<String[]> courses = DBManager.getAllCourses();

        //filter
        studentIds = students.stream()
                .map(arr -> arr[0])   // lấy ID
                .toList();

        courseIds = courses.stream()
                .map(arr -> arr[0])
                .toList();


        // --- Panel Form ---
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dropdown Student ID
        formPanel.add(new JLabel("Student ID:"));
        cbStudentId = new JComboBox<>(studentIds.toArray(new String[0]));
        cbStudentId.setEditable(true);
        cbStudentId.setSelectedItem(null);
        AutoCompleteCombo.enable(cbStudentId, studentIds);
        formPanel.add(cbStudentId);

        // Dropdown Course ID
        formPanel.add(new JLabel("Course ID:"));
        cbCourseId = new JComboBox<>(courseIds.toArray(new String[0]));
        cbCourseId.setEditable(true);
        cbCourseId.setSelectedItem(null);
        AutoCompleteCombo.enable(cbCourseId, courseIds);
        formPanel.add(cbCourseId);

        // Input Score
        formPanel.add(new JLabel("Score (0.0 to 10.0):"));
        txtScore = new JTextField(5);
        formPanel.add(txtScore);

        win.add(formPanel, BorderLayout.CENTER);

        // --- Panel Button ---
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Score");
        btnAdd.addActionListener(this);
        // Vô hiệu hóa nút nếu không có Student hoặc Course để chọn
        if (studentIds.isEmpty() || courseIds.isEmpty()) {
            btnAdd.setEnabled(false);
            buttonPanel.add(new JLabel("Cần ít nhất 1 Student và 1 Course."));
        }
        buttonPanel.add(btnAdd);


        win.add(buttonPanel, BorderLayout.SOUTH);
        win.setResizable(false);

        display();
    }

    public void display(){
        win.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addNewScore();
        }
    }

    private void addNewScore() {
        String studentId = (String) cbStudentId.getSelectedItem();
        String courseId = (String) cbCourseId.getSelectedItem();
        String scoreStr = txtScore.getText().trim();

        // Kiểm tra lựa chọn và rỗng
        if (studentId == null || courseId == null || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(win, "Please select Student/Course and enter a Score.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!studentIds.contains(studentId)) {
            JOptionPane.showMessageDialog(win, "Please select an existing Student.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!courseIds.contains(courseId)) {
            JOptionPane.showMessageDialog(win, "Please select an existing Course.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validation Score (phải là số thực từ 0.0 đến 10.0)
        double score;
        try {
            score = Double.parseDouble(scoreStr);
            if (score < 0.0 || score > 10.0) {
                JOptionPane.showMessageDialog(win, "Score must be between 0.0 and 10.0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(win, "Invalid Score format. Please enter a number (e.g., 8.5).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thực hiện thêm vào DB
        try {
            double fomatScore = (double) Math.round(score * 100) /100;
            DBManager.addScore(studentId, courseId, fomatScore);
            JOptionPane.showMessageDialog(win, "Score added successfully for Student " + studentId + " in Course " + courseId + ".", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Refresh bảng Scores trong cửa sổ MainApp
            mainApp.loadScoreData();

            // Đóng cửa sổ dialog
            win.dispose();

        } catch (SQLException ex) {
            // Lỗi SQL có thể là do ràng buộc khóa ngoại (nếu không được xử lý bên DBManager)
            JOptionPane.showMessageDialog(win, "Database Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}