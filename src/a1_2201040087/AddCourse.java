package a1_2201040087;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddCourse extends JDialog implements ActionListener {
    private JTextField txtId;
    private JTextField txtName;
    private JButton btnAdd;
    private MainApp mainApp;

    public AddCourse(JFrame parent, MainApp mainApp) {
        super(parent, "Add New Course", true);
        this.mainApp = mainApp;
        setLayout(new BorderLayout(10, 10));
        setSize(350, 200);
        setLocationRelativeTo(parent);

        // --- Panel Form ---
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Course ID (9 alphanumeric):"));
        txtId = new JTextField(10);
        formPanel.add(txtId);

        formPanel.add(new JLabel("Course Name:"));
        txtName = new JTextField(10);
        formPanel.add(txtName);

        add(formPanel, BorderLayout.CENTER);

        // --- Panel Button ---
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Course");
        btnAdd.addActionListener(this);
        buttonPanel.add(btnAdd);

        add(buttonPanel, BorderLayout.SOUTH);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addNewCourse();
        }
    }

    private void addNewCourse() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();

        // 1. Kiểm tra rỗng
        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validate Mã môn học (9 ký tự, gồm cả chữ và số)
        //
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{9}$");
        if (!pattern.matcher(id).matches()) {
            JOptionPane.showMessageDialog(this,
                    "Course ID must be 9 characters long and contain only letters and numbers (e.g., 61FIT3JSD).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validate Name chỉ chứa chữ cái (Unicode) và khoảng trắng
        Pattern namePattern = Pattern.compile("^[\\p{L}\\s]+$");
        if (!namePattern.matcher(name).matches()) {
            JOptionPane.showMessageDialog(this,
                    "Name can only contain letters and spaces.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // 3. Thực hiện thêm vào DB
        try {
            DBManager.addCourse(id, name);
            JOptionPane.showMessageDialog(this, "Course " + name + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // 4. Refresh bảng Courses trong cửa sổ MainApp
            mainApp.loadCourseData();

            // Đóng cửa sổ dialog
            dispose();

        } catch (SQLException ex) {
            // Lỗi SQL có thể là do trùng ID (Primary Key Constraint)
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "Error: Course ID " + id + " already exists.", "DB Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}