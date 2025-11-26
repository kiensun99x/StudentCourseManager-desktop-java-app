package a1_2201040087;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddStudent extends JDialog implements ActionListener {
    private JTextField txtId;
    private JTextField txtName;
    private JButton btnAdd;
    private MainApp mainApp; // Tham chiếu đến MainApp để refresh bảng

    public AddStudent(JFrame parent, MainApp mainApp) {
        super(parent, "Add New Student", true);
        this.mainApp = mainApp;
        setLayout(new BorderLayout(10, 10));
        setSize(350, 200);
        setLocationRelativeTo(parent);

        // --- Panel Form ---
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Student ID (10 digits):"));
        txtId = new JTextField(10);
        formPanel.add(txtId);

        formPanel.add(new JLabel("Student Name:"));
        txtName = new JTextField(10);
        formPanel.add(txtName);

        add(formPanel, BorderLayout.CENTER);

        // --- Panel Button ---
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Student");
        btnAdd.addActionListener(this);
        buttonPanel.add(btnAdd);

        add(buttonPanel, BorderLayout.SOUTH);
        setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            addNewStudent();
        }
    }

    private void addNewStudent() {
        String idStr = txtId.getText().trim();
        String name = txtName.getText().trim();

        // 1. Kiểm tra rỗng
        if (idStr.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Student ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validate Mã sinh viên (10 chữ số)
        //
        Pattern pattern = Pattern.compile("^\\d{10}$");
        if (!pattern.matcher(idStr).matches()) {
            JOptionPane.showMessageDialog(this,
                    "Student ID must be a 10-digit number (e.g., 2201040001).",
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
            // ID sinh viên là số lớn, nên ta phải parse sang Long
            long studentId = Long.parseLong(idStr);

            DBManager.addStudent(studentId, name);
            JOptionPane.showMessageDialog(this, "Student " + name + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // 4. Refresh bảng Students trong cửa sổ MainApp
            mainApp.loadStudentData();

            // Đóng cửa sổ dialog
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Internal Error: ID could not be converted to Long.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            // Lỗi SQL có thể là do trùng ID (Primary Key Constraint)
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(this, "Error: Student ID " + idStr + " already exists.", "DB Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}