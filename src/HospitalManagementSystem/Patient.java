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

    // Constructor for Patient class
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
        JButton checkPatientButton = new JButton("Search Patient");

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

    // Empty constructor
    public Patient(Connection connection) {
    }

    // Method to add a new patient
    private void addPatient() {
        String name = JOptionPane.showInputDialog("Enter Patient Name:");
        String ageString = JOptionPane.showInputDialog("Enter Patient Age:");

        // Check if ageString is not null and not empty
        if (ageString != null && !ageString.isEmpty()) {
            try {
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
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid age format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Handle the case where the ageString is empty
            JOptionPane.showMessageDialog(this, "Bruh Do your Work and Add The Required Information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to view all patients
    void viewPatients() {
        String query = "SELECT * FROM patients ";

        try {
            // SQL query to select all patients from the 'patients' table
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Display the list of patients in a JOptionPane
            StringBuilder patientInfo = new StringBuilder("Patients:\n");
            patientInfo.append("|------------------------------------------------------------------|\n");
            patientInfo.append("|Patient Id| Name                       | Age       | Gender       |\n");
            patientInfo.append("|------------------------------------------------------------------|\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                // Append patient information to the StringBuilder
                patientInfo.append(String.format("| %-12s | %-25s | %-7s | %-8s\n", id, name, age, gender));
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

    // Method to check a patient by ID
    private void checkPatient() {
        String idString = JOptionPane.showInputDialog("Enter Patient ID:");

        // Check if idString is not null and not empty
        if (idString != null && !idString.isEmpty()) {
            try {
                int id = Integer.parseInt(idString);

                try {
                    // SQL query to select a patient by ID from the 'patients' table (replace with your actual table name)
                    String query = "SELECT name FROM patients WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    // Check if a patient with the given ID exists
                    if (resultSet.next()) {
                        String patientName = resultSet.getString("name");
                        JOptionPane.showMessageDialog(this, patientName);
                    } else {
                        JOptionPane.showMessageDialog(this, "Patient with ID " + id + " does not exist!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number for Patient ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Handle the case where the idString is empty
            JOptionPane.showMessageDialog(this, "Patient ID cannot be empty. Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to run the program
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
