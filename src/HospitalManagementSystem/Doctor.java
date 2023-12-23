package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Scanner;

public class Doctor extends JFrame {
    private Connection connection;
    private Scanner scanner;

    // Constructor for Doctor class
    public Doctor(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;

        // Set up the frame
        setTitle("Doctor Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Create buttons
        JButton addDoctorButton = new JButton("Add Doctor");
        JButton viewDoctorsButton = new JButton("View Doctors");
        JButton checkDoctorButton = new JButton("Check Doctor");

        // Set up the layout
        setLayout(new GridLayout(4, 1));
        add(addDoctorButton);
        add(viewDoctorsButton);
        add(checkDoctorButton);

        // Add action listeners to the buttons
        addDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDoctor();
            }
        });

        viewDoctorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewDoctors();
            }
        });

        checkDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkDoctor();
            }
        });
    }

    // Method to add a new doctor
    private void addDoctor() {
        String name = JOptionPane.showInputDialog("Enter Doctor Name:");
        String specialization = JOptionPane.showInputDialog("Enter Specialization:");
        String enrollmentDate = JOptionPane.showInputDialog("Enter Enrollment Date (YYYY-MM-DD):");

        // Check if enrollmentDate is not null
        if (enrollmentDate != null) {
            try {
                // SQL query to insert a new doctor into the 'new_doctors' table
                String query = "INSERT INTO new_doctors(name, specialization, Enrollment_Date) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, specialization);
                preparedStatement.setString(3, enrollmentDate);

                // Execute the query and check if the doctor was added successfully
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Doctor Added Successfully!!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add Doctor!!");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Handle the case where enrollmentDate is null
            JOptionPane.showMessageDialog(this, "Don't slack off Add The Required Information.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to view all doctors
    void viewDoctors() {
        String query = "SELECT * FROM new_doctors";

        try {
            // SQL query to select all doctors from the 'new_doctors' table
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Display the list of doctors in a JOptionPane
            StringBuilder doctorInfo = new StringBuilder("Doctors:\n");
            doctorInfo.append("|-----------------------------------------------------------------------------|\n");
            doctorInfo.append("| Doctor Id | Name             | Specialization     | Enrollment Date         |\n");
            doctorInfo.append("|-----------------------------------------------------------------------------|\n");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                String enrollmentDate = resultSet.getString("Enrollment_Date");

                // Append doctor information to the StringBuilder
                doctorInfo.append(String.format("| %-10s | %-18s | %-16s | %-17s\n", id, name, specialization, enrollmentDate));
                doctorInfo.append("|------------------------------------------------------------------------------|\n");
            }

            // Display the information in a JOptionPane
            JTextArea textArea = new JTextArea(doctorInfo.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "View Doctors", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to check a doctor by ID
    private void checkDoctor() {
        String idString = JOptionPane.showInputDialog("Enter Doctor ID:");
        int id = Integer.parseInt(idString);

        try {
            // SQL query to select a doctor by ID from the 'new_doctors' table
            String query = "SELECT name FROM new_doctors WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a doctor with the given ID exists
            if (resultSet.next()) {
                String doctorName = resultSet.getString("name");
                JOptionPane.showMessageDialog(this, doctorName);
            } else {
                JOptionPane.showMessageDialog(this, "Doctor with ID " + id + " does not exist!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                    new Doctor(connection, scanner).setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Dummy method
    public boolean getDoctorById(int doctorId) {
        return false;
    }
}
