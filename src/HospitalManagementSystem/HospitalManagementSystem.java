package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem extends JFrame {
    private Connection connection;
    private Scanner scanner;
    private Patient patientGUI;
    private Doctor doctorGUI;
    private JPanel mainPanel;

    public HospitalManagementSystem(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;

        // Set up the frame
        setTitle("Hospital Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        // Create components for login GUI
        JLabel userLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField userTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Set up the layout for login GUI
        setLayout(new GridLayout(4, 2));
        add(userLabel);
        add(userTextField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);

        // Add action listener for the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText().trim();
                String password = new String(passwordField.getPassword());

                // Replace with your actual login logic
                if (isValidLogin(username, password)) {
                    showMainGUI();
                } else {
                    JOptionPane.showMessageDialog(HospitalManagementSystem.this, "Invalid username or password");
                }
            }
        });
    }

    // Method to check the validity of the login credentials (replace with your actual implementation)
    private boolean isValidLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    // Method to show the main functionality GUI
    private void showMainGUI() {
        // Create a new JPanel for the main functionality
        mainPanel = new JPanel();

        // Create buttons for the main functionality
        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton addDoctorButton = new JButton("Add Doctor");
        JButton viewDoctorsButton = new JButton("View Doctors");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton exitButton = new JButton("Exit");

        // Set up the layout for main functionality GUI
        mainPanel.setLayout(new GridLayout(6, 1));
        mainPanel.add(addPatientButton);
        mainPanel.add(viewPatientsButton);
        mainPanel.add(addDoctorButton);
        mainPanel.add(viewDoctorsButton);
        mainPanel.add(bookAppointmentButton);
        mainPanel.add(exitButton);

        // Create instances of PatientGUI and DoctorGUI
        patientGUI = new Patient(connection, scanner);
        doctorGUI = new Doctor(connection, scanner);

        // Add action listeners to the buttons
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientGUI.setVisible(true);
            }
        });

        viewPatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientGUI.viewPatients();
            }
        });

        addDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doctorGUI.setVisible(true);
            }
        });

        viewDoctorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doctorGUI.viewDoctors();
            }
        });

        bookAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Set the content pane to the new JPanel
        setContentPane(mainPanel);

        // Refresh the frame to show the new components
        revalidate();
        repaint();
    }

    // ... (rest of the code)

    private void bookAppointment() {
        // ... (rest of the code)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Connection connection;
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "abu123");
                    Scanner scanner = new Scanner(System.in);
                    new HospitalManagementSystem(connection, scanner).setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
