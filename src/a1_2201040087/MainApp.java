package a1_2201040087;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class MainApp extends WindowAdapter implements ActionListener {
    private JFrame frame;
    // Khai báo các thành phần giao diện
    private JRadioButton radStudents, radCourses, radScores;
    private ButtonGroup radioGroup;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JButton btnAddStudent, btnAddCourse, btnAddScore;

    public MainApp() {
        frame = new JFrame();
        // Window setting
        frame.setTitle("F2025 JSD Assignment");
        frame.setSize(600, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // Top Panel (Radio Buttons)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radStudents = new JRadioButton("Students", true); // Mặc định chọn Students
        radCourses = new JRadioButton("Courses");
        radScores = new JRadioButton("Scores");

        radStudents.addActionListener(this);
        radCourses.addActionListener(this);
        radScores.addActionListener(this);

        // Gom nhóm để chỉ chọn được 1 cái tại 1 thời điểm
        radioGroup = new ButtonGroup();
        radioGroup.add(radStudents);
        radioGroup.add(radCourses);
        radioGroup.add(radScores);

        topPanel.add(radStudents);
        topPanel.add(radCourses);
        topPanel.add(radScores);

        //Right Panel chứa các nút bấm
        JPanel rightPanel = new JPanel();
//        rightPanel.setPreferredSize( new Dimension(120,300));
        rightPanel.setLayout(new GridLayout(4, 1, 10, 10)); // 4 dòng, 1 cột, khoảng cách 10
//        rightPanel.setLayout(new FlowLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Căn lề phải 1 chút

        btnAddStudent = new JButton("Add Student");
        btnAddCourse = new JButton("Add Course");
        btnAddScore = new JButton("Add Score");

        btnAddStudent.addActionListener(this);
        btnAddCourse.addActionListener(this);
        btnAddScore.addActionListener(this);

        // Chỉnh kích thước nút
        Dimension btnSize = new Dimension(120, 30);
        btnAddStudent.setPreferredSize(btnSize);
        btnAddCourse.setPreferredSize(btnSize);
        btnAddScore.setPreferredSize(btnSize);

        rightPanel.add(btnAddStudent);
        rightPanel.add(btnAddCourse);
        rightPanel.add(btnAddScore);

        // Tạo bảng dữ liệu
        tableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return getColumnName(column).equalsIgnoreCase("Action");
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(dataTable);


        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        // 6. Xử lý sự kiện (Logic)
//        addEvents();

        // 7. Tải dữ liệu mặc định ban đầu (Students)
        loadStudentData();

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        System.out.println(command);
        if (command.equalsIgnoreCase("students")){
            loadStudentData();
        } else if (command.equalsIgnoreCase("courses")){
            loadCourseData();
        } else if (command.equalsIgnoreCase("scores")) {
            loadScoreData();
        } else if (command.equalsIgnoreCase("add student")) {

        } else if (command.equalsIgnoreCase("add course")) {

        } else if (command.equalsIgnoreCase("add score")) {

        } else if (command.equalsIgnoreCase("delete")) {

        }
    }

//    private void addEvents() {
//        // Sự kiện khi bấm vào Radio Button
//        ActionListener radioListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (radStudents.isSelected()) {
//                    loadStudentData();
//                } else if (radCourses.isSelected()) {
//                    loadCourseData();
//                } else if (radScores.isSelected()) {
//                    loadScoreData();
//                }
//            }
//        };
//
//        radStudents.addActionListener(radioListener);
//        radCourses.addActionListener(radioListener);
//        radScores.addActionListener(radioListener);
//
//        // Sự kiện cho các nút Add (Chỉ hiện thông báo demo)
//        btnAddStudent.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Open window: Add Student"));
//        btnAddCourse.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Open window: Add Course"));
//        btnAddScore.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Open window: Add Score"));
//    }

    // --- Các hàm giả lập dữ liệu (Sau này bạn sẽ thay bằng JDBC kết nối database) ---

    private void loadStudentData() {
        // Xóa dữ liệu cũ và cột cũ
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Thêm cột cho bảng Student
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Action");

        // Thêm dữ liệu giả giống hình ảnh
        tableModel.addRow(new Object[]{"2201040210", "John", "Delete"});
        tableModel.addRow(new Object[]{"2201040011", "David", "Delete"});
        tableModel.addRow(new Object[]{"2201040005", "Alex", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});
        tableModel.addRow(new Object[]{"2201040100", "Mary", "Delete"});

        dataTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        dataTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void loadCourseData() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.addColumn("Course ID");
        tableModel.addColumn("Subject");
        tableModel.addColumn("Action");

        tableModel.addRow(new Object[]{"C001", "Java Programming", "delete"});
        tableModel.addRow(new Object[]{"C002", "Database", "delete"});

        dataTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        dataTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

    }

    private void loadScoreData() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        tableModel.addColumn("ID");
        tableModel.addColumn("Student ID");
        tableModel.addColumn("Course ID");
        tableModel.addColumn("Score");
        tableModel.addColumn("Action");

        tableModel.addRow(new Object[]{"1", "2201040210", "C001", 8.5,"delete"});
        tableModel.addRow(new Object[]{"2", "2201040011", "C002", 7.0,"delete"});

        dataTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        dataTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow; // Lưu lại dòng đang bấm

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            // Khi nút được bấm
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped(); // Báo cho bảng biết là đã dừng edit

                    // Xử lý xóa dòng ở đây
                    int confirm = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to delete row " + currentRow + "?",
                            "Confirm Delete", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        // Xóa dòng khỏi model
                        if (currentRow >= 0 && currentRow < tableModel.getRowCount()) {
                            tableModel.removeRow(currentRow);
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            currentRow = row;
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
    }

    public static void main(String[] args) {
        new MainApp();
    }


}
