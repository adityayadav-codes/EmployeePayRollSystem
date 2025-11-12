import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class PayrollSystem extends JFrame implements ActionListener {

    // Panels
    JPanel loginPanel, adminLoginPanel, empLoginPanel, adminPanel, employeePanel;
    CardLayout cardLayout;

    // Database Connection
    Connection con;

    // Admin Login
    JTextField txtAdminUser;
    JPasswordField txtAdminPass;
    JButton btnAdminLogin;

    // Employee Login
    JTextField txtEmpUser;
    JPasswordField txtEmpPass;
    JButton btnEmpLogin;

    // Admin components
    JTextField txtId, txtName, txtBasic, txtDeductions, txtOt, txtAttendance;
    JButton btnAdd, btnUpdate, btnLogoutAdmin;
    JTextArea adminArea;

    // Employee components
    JTextField txtEmpId;
    JButton btnView, btnLogoutEmp;
    JTextArea empArea;

    public PayrollSystem() {
        setTitle("Employee Payroll Management System");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        connectDatabase();
        createLoginPanel();
        createAdminPanel();
        createEmployeePanel();

        add(loginPanel, "Login");
        add(adminPanel, "Admin");
        add(employeePanel, "Employee");

        cardLayout.show(getContentPane(), "Login");
    }
// DB connection
     public void connectDatabase() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));
            System.out.println("✅ Properties loaded successfully!");

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to DB successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Database connection failed!\n" + e.getMessage());
            System.exit(0);
        }
    }

    //login panel

    void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(1, 2));

        // ------------------- ADMIN LOGIN -------------------
        adminLoginPanel = new JPanel(new GridBagLayout());
        adminLoginPanel.setBorder(BorderFactory.createTitledBorder("Admin Login"));
        GridBagConstraints a = new GridBagConstraints();
        a.insets = new Insets(10, 10, 10, 10);

        JLabel lblAdminUser = new JLabel("Username:");
        JLabel lblAdminPass = new JLabel("Password:");
        txtAdminUser = new JTextField(15);
        txtAdminPass = new JPasswordField(15);
        btnAdminLogin = new JButton("Login as Admin");
        btnAdminLogin.addActionListener(this);

        a.gridx = 0; a.gridy = 0; adminLoginPanel.add(lblAdminUser, a);
        a.gridx = 1; adminLoginPanel.add(txtAdminUser, a);
        a.gridx = 0; a.gridy = 1; adminLoginPanel.add(lblAdminPass, a);
        a.gridx = 1; adminLoginPanel.add(txtAdminPass, a);
        a.gridx = 0; a.gridy = 2; a.gridwidth = 2;
        adminLoginPanel.add(btnAdminLogin, a);

        // ------------------- EMPLOYEE LOGIN -------------------
        empLoginPanel = new JPanel(new GridBagLayout());
        empLoginPanel.setBorder(BorderFactory.createTitledBorder("Employee Login"));
        GridBagConstraints e = new GridBagConstraints();
        e.insets = new Insets(10, 10, 10, 10);

        JLabel lblEmpUser = new JLabel("Username:");
        JLabel lblEmpPass = new JLabel("Password:");
        txtEmpUser = new JTextField(15);
        txtEmpPass = new JPasswordField(15);
        btnEmpLogin = new JButton("Login as Employee");
        btnEmpLogin.addActionListener(this);

        e.gridx = 0; e.gridy = 0; empLoginPanel.add(lblEmpUser, e);
        e.gridx = 1; empLoginPanel.add(txtEmpUser, e);
        e.gridx = 0; e.gridy = 1; empLoginPanel.add(lblEmpPass, e);
        e.gridx = 1; empLoginPanel.add(txtEmpPass, e);
        e.gridx = 0; e.gridy = 2; e.gridwidth = 2;
        empLoginPanel.add(btnEmpLogin, e);

        loginPanel.add(adminLoginPanel);
        loginPanel.add(empLoginPanel);
    }

    void createAdminPanel() {
        adminPanel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new GridLayout(7, 2, 5, 5));

        topPanel.add(new JLabel("Employee ID:"));
        txtId = new JTextField();
        topPanel.add(txtId);

        topPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        topPanel.add(txtName);

        topPanel.add(new JLabel("Basic Salary:"));
        txtBasic = new JTextField();
        topPanel.add(txtBasic);

        topPanel.add(new JLabel("Deductions:"));
        txtDeductions = new JTextField();
        topPanel.add(txtDeductions);

        topPanel.add(new JLabel("Overtime Hours:"));
        txtOt = new JTextField();
        topPanel.add(txtOt);

        topPanel.add(new JLabel("Attendance (days):"));
        txtAttendance = new JTextField();
        topPanel.add(txtAttendance);

        btnAdd = new JButton("Add Employee");
        btnUpdate = new JButton("Update Employee");
        btnLogoutAdmin = new JButton("Logout");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnLogoutAdmin);

        adminArea = new JTextArea();
        adminArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(adminArea);

        adminPanel.add(topPanel, BorderLayout.NORTH);
        adminPanel.add(scroll, BorderLayout.CENTER);
        adminPanel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnLogoutAdmin.addActionListener(this);
    }

    void createEmployeePanel() {
        employeePanel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout());
        JLabel lbl = new JLabel("Enter Your Employee ID:");
        txtEmpId = new JTextField(10);
        btnView = new JButton("View Details");
        btnLogoutEmp = new JButton("Logout");

        top.add(lbl);
        top.add(txtEmpId);
        top.add(btnView);
        top.add(btnLogoutEmp);

        empArea = new JTextArea();
        empArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(empArea);

        employeePanel.add(top, BorderLayout.NORTH);
        employeePanel.add(scroll, BorderLayout.CENTER);

        btnView.addActionListener(this);
        btnLogoutEmp.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == btnAdminLogin) {
                adminLogin();
            } else if (e.getSource() == btnEmpLogin) {
                employeeLogin();
            } else if (e.getSource() == btnAdd) {
                addEmployee();
            } else if (e.getSource() == btnUpdate) {
                updateEmployee();
            } else if (e.getSource() == btnView) {
                viewEmployee();
            } else if (e.getSource() == btnLogoutAdmin || e.getSource() == btnLogoutEmp) {
                cardLayout.show(getContentPane(), "Login");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    void adminLogin() {
        String user = txtAdminUser.getText().trim();
        String pass = new String(txtAdminPass.getPassword()).trim();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT role FROM users WHERE username=? AND password=?");
            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getString("role").equals("admin")) {
                cardLayout.show(getContentPane(), "Admin");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Login!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Login Error: " + e.getMessage());
        }
    }

    void employeeLogin() {
        String user = txtEmpUser.getText().trim();
        String pass = new String(txtEmpPass.getPassword()).trim();
        try {
            PreparedStatement pst = con.prepareStatement("SELECT role FROM users WHERE username=? AND password=?");
            pst.setString(1, user);
            pst.setString(2, pass);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getString("role").equals("employee")) {
                cardLayout.show(getContentPane(), "Employee");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Employee Login!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Login Error: " + e.getMessage());
        }
    }

    void addEmployee() {
        try {
            PreparedStatement pst = con.prepareStatement("INSERT INTO employees VALUES(?, ?, ?, ?, ?, ?)");
            pst.setInt(1, Integer.parseInt(txtId.getText()));
            pst.setString(2, txtName.getText());
            pst.setDouble(3, Double.parseDouble(txtBasic.getText()));
            pst.setDouble(4, Double.parseDouble(txtDeductions.getText()));
            pst.setInt(5, Integer.parseInt(txtOt.getText()));
            pst.setInt(6, Integer.parseInt(txtAttendance.getText()));
            pst.executeUpdate();
            adminArea.setText("✅ Employee Added Successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Add Error: " + ex.getMessage());
        }
    }

    void updateEmployee() {
        try {
            PreparedStatement pst = con.prepareStatement(
                "UPDATE employees SET name=?, basic=?, deductions=?, ot=?, attendance=? WHERE id=?"
            );
            pst.setString(1, txtName.getText());
            pst.setDouble(2, Double.parseDouble(txtBasic.getText()));
            pst.setDouble(3, Double.parseDouble(txtDeductions.getText()));
            pst.setInt(4, Integer.parseInt(txtOt.getText()));
            pst.setInt(5, Integer.parseInt(txtAttendance.getText()));
            pst.setInt(6, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            adminArea.setText("✅ Employee Updated Successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage());
        }
    }

    void viewEmployee() {
        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM employees WHERE id=?");
            pst.setInt(1, Integer.parseInt(txtEmpId.getText()));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                double basic = rs.getDouble("basic");
                double deductions = rs.getDouble("deductions");
                int ot = rs.getInt("ot");
                int attendance = rs.getInt("attendance");

                double total = basic + (ot * 100) - deductions;
                total = total * (attendance / 30.0);

                empArea.setText("Employee Details:\n");
                empArea.append("Name: " + rs.getString("name") + "\n");
                empArea.append("Basic: " + basic + "\n");
                empArea.append("OT Hours: " + ot + "\n");
                empArea.append("Attendance: " + attendance + " days\n");
                empArea.append("Deductions: " + deductions + "\n");
                empArea.append("------------------------\n");
                empArea.append("Net Pay: ₹" + String.format("%.2f", total));
            } else {
                empArea.setText("No employee found!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "View Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PayrollSystem().setVisible(true));
    }
}