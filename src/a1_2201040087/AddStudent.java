package a1_2201040087;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class AddStudent extends WindowAdapter implements ActionListener {
    private JFrame win;
    private JFrame parentGUI;
    private JTextField txtId;
    private JTextField txtName;
    private JButton btnAdd;
    private MainApp mainApp; // Tham chiếu đến MainApp để refresh bảng

    public AddStudent(JFrame parent, MainApp mainApp){
        this.parentGUI = parent;
        this.mainApp = mainApp;
        createGUI();
    }

    private void createGUI(){
        win = new JFrame("Add New Student");
        win.setLayout(new BorderLayout(10, 10));
        win.setSize(350, 200);
        win.setLocationRelativeTo(parentGUI);

        // --- Panel Form ---
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Student ID (10 digits):"));
        txtId = new JTextField(10);
        formPanel.add(txtId);

        formPanel.add(new JLabel("Student Name:"));
        txtName = new JTextField(10);
        formPanel.add(txtName);

        win.add(formPanel, BorderLayout.CENTER);

        // --- Panel Button ---
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add Student");
        btnAdd.addActionListener(this);
        buttonPanel.add(btnAdd);

        win.add(buttonPanel, BorderLayout.SOUTH);
        win.setResizable(false);

        display();
    }

    public void display(){
        win.setVisible(true);
    }
//    public AddStudent(JFrame parent, MainApp mainApp) {
//        super(parent, "Add New Student", true);
//        this.mainApp = mainApp;
//        setLayout(new BorderLayout(10, 10));
//        setSize(350, 200);
//        setLocationRelativeTo(parent);
//
//        // --- Panel Form ---
//        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
//        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        formPanel.add(new JLabel("Student ID (10 digits):"));
//        txtId = new JTextField(10);
//        formPanel.add(txtId);
//
//        formPanel.add(new JLabel("Student Name:"));
//        txtName = new JTextField(10);
//        formPanel.add(txtName);
//
//        add(formPanel, BorderLayout.CENTER);
//
//        // --- Panel Button ---
//        JPanel buttonPanel = new JPanel();
//        btnAdd = new JButton("Add Student");
//        btnAdd.addActionListener(this);
//        buttonPanel.add(btnAdd);
//
//        add(buttonPanel, BorderLayout.SOUTH);
//        setResizable(false);
//    }

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
            JOptionPane.showMessageDialog(win, "Student ID and Name cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validate Mã sinh viên (10 chữ số)
        //
        Pattern pattern = Pattern.compile("^\\d{10}$");
        if (!pattern.matcher(idStr).matches()) {
            JOptionPane.showMessageDialog(win,
                    "Student ID must be a 10-digit number (e.g., 2201040001).",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Validate Name chỉ chứa chữ cái (Unicode) và khoảng trắng
        Pattern namePattern = Pattern.compile("^[\\p{L}\\s]+$");
        if (!namePattern.matcher(name).matches()) {
            JOptionPane.showMessageDialog(win,
                    "Name can only contain letters and spaces.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // 3. Thực hiện thêm vào DB
        try {
            // ID sinh viên là số lớn, nên ta phải parse sang Long
            long studentId = Long.parseLong(idStr);

            DBManager.addStudent(studentId, name);
            JOptionPane.showMessageDialog(win, "Student " + name + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // 4. Refresh bảng Students trong cửa sổ MainApp
            mainApp.loadStudentData();

            // Đóng cửa sổ dialog
            win.dispose();
            mainApp.loadStudentData();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(win, "Internal Error: ID could not be converted to Long.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            // Lỗi SQL có thể là do trùng ID (Primary Key Constraint)
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                JOptionPane.showMessageDialog(win, "Error: Student ID " + idStr + " already exists.", "DB Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(win, "Database Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}