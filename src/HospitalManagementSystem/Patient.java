package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Scanner;

public class Patient extends JFrame {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;

        // Set up the frame
        setTitle("Patient Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Create buttons
        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton checkPatientButton = new JButton("Check Patient");

        // Set up the layout
        setLayout(new GridLayout(3, 1));
        add(addPatientButton);
        add(viewPatientsButton);
        add(checkPatientButton);

        // Add action listeners to the buttons
        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        viewPatientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPatients();
            }
        });

        checkPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPatient();
            }
        });
    }

    public Patient(Connection connection) {
    }

    private void addPatient() {
        String name = JOptionPane.showInputDialog("Enter Patient Name:");
        String ageString = JOptionPane.showInputDialog("Enter Patient Age:");
        int age = Integer.parseInt(ageString);
        String gender = JOptionPane.showInputDialog("Enter Patient Gender:");

        try {
            // SQL query to insert a new patient into the 'patients' table
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            // Execute the query and check if the patient was added successfully
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Patient Added Successfully!!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add Patient!!");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void viewPatients() {
        String query = "SELECT * FROM patients ";

        try {
            // SQL query to select all patients from the 'patients' table
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Display the list of patients in a JOptionPane
            StringBuilder patientInfo = new StringBuilder("Patients:\n");
            patientInfo.append("|-------------------------------------------------------|\n");
            patientInfo.append("| Patient Id| Name               | Age   | Gender       |\n");
            patientInfo.append("|-------------------------------------------------------|\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                // Append patient information to the StringBuilder
                patientInfo.append(String.format("| %-11s | %-20s | %-7s | %-8s |\n", id, name, age, gender));
                patientInfo.append("|----------------------------------------------------------|\n");
            }

            // Display the information in a JOptionPane
            JTextArea textArea = new JTextArea(patientInfo.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "View Patients", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkPatient() {
        String idString = JOptionPane.showInputDialog("Enter Patient ID:");
        int id = Integer.parseInt(idString);

        try {
            // SQL query to select a patient by ID from the 'patients' table
            String query = "SELECT * FROM patients WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a patient with the given ID exists
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Patient with ID " + id + " exists!");
            } else {
                JOptionPane.showMessageDialog(this, "Patient with ID " + id + " does not exist!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Replace "your_database", "your_username", and "your_password" with your actual MySQL details
                Connection connection;
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital", "root", "abu123");
                    Scanner scanner = new Scanner(System.in);
                    new Patient(connection, scanner).setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean getPatientById(int patientId) {
        return false;
    }
}
