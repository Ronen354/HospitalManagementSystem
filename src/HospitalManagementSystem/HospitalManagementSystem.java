package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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

        setTitle("Hospital Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JLabel userLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField userTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        setLayout(new GridLayout(4, 2));
        add(userLabel);
        add(userTextField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty label for spacing
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userTextField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (isValidLogin(username, password)) {
                    showMainGUI();
                } else {
                    JOptionPane.showMessageDialog(HospitalManagementSystem.this, "Invalid username or password");
                }
            }
        });
    }

    private boolean isValidLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    private void showMainGUI() {
        mainPanel = new JPanel();

        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton addDoctorButton = new JButton("Add Doctor");
        JButton viewDoctorsButton = new JButton("View Doctors");
        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton exitButton = new JButton("Exit");

        mainPanel.setLayout(new GridLayout(7, 1));
        mainPanel.add(addPatientButton);
        mainPanel.add(viewPatientsButton);
        mainPanel.add(addDoctorButton);
        mainPanel.add(viewDoctorsButton);
        mainPanel.add(viewAppointmentsButton);
        mainPanel.add(bookAppointmentButton);
        mainPanel.add(exitButton);

        patientGUI = new Patient(connection, scanner);
        doctorGUI = new Doctor(connection, scanner);

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
        viewAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAppointments();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    private void bookAppointment() {
        JTextField patientIdField = new JTextField();
        JTextField doctorIdField = new JTextField();

        Object[] message = {
                "Patient ID:", patientIdField,
                "Doctor ID:", doctorIdField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Book Appointment", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                int doctorId = Integer.parseInt(doctorIdField.getText());
                JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for Patient ID and Doctor ID.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Do your job for humanity's sake.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAppointments() {
        try {
            String query = "SELECT * FROM appointments";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                StringBuilder appointmentInfo = new StringBuilder("Appointments:\n");
                appointmentInfo.append("|-----------------------------------------------------------------------------|\n");
                appointmentInfo.append("| Appointment ID | Patient ID | Doctor ID | Appointment Date         |\n");
                appointmentInfo.append("|-----------------------------------------------------------------------------|\n");

                do {
                    try {
                        int appointmentId = resultSet.getInt("id");
                        int patientId = resultSet.getInt("patient_id");
                        int doctorId = resultSet.getInt("doctor_id");
                        String appointmentDate = resultSet.getString("appointment_date");

                        appointmentInfo.append(String.format("| %-15s | %-10s | %-9s | %-25s\n", appointmentId, patientId, doctorId, appointmentDate));
                        appointmentInfo.append("|------------------------------------------------------------------------------|\n");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error retrieving appointment information: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } while (resultSet.next());

                JTextArea textArea = new JTextArea(appointmentInfo.toString());
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "View Appointments", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No appointments found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error executing SQL query: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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
