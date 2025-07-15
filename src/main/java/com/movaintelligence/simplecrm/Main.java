package com.movaintelligence.simplecrm;

import com.movaintelligence.simplecrm.view.CustomerListView;
import com.movaintelligence.simplecrm.repository.CustomerRepositoryImpl;
import com.movaintelligence.simplecrm.repository.CustomerRepository;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to default
        }

        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize H2 in-memory database
                Connection conn = DriverManager.getConnection(
                    "jdbc:h2:mem:simplecrm;DB_CLOSE_DELAY=-1", "sa", ""
                );
                initializeDatabase(conn);
                CustomerRepository customerRepo = new CustomerRepositoryImpl(conn);

                // Pass repository to view or service as needed
                CustomerListView view = new CustomerListView(customerRepo);
                view.setVisible(true);
            }
            catch (SQLException e) {
                e.printStackTrace(System.out);
                JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
            }
        });
    }

    private static void initializeDatabase(Connection conn) throws SQLException {
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream("schema.sql");
            if (is == null)
                throw new RuntimeException("schema.sql not found in resources");

            var ddlFileContent = new StringBuilder();
            try (var reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ddlFileContent.append(line).append("\n");
                }
            }

            String[] statements = ddlFileContent.toString().split(";");
            try (var st = conn.createStatement()) {
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty()) {
                        st.execute(trimmed);
                    }
                }
            }

            System.out.println("Database initialized successfully from schema.sql");
        }
        catch (Exception ex) {
            throw new SQLException("Failed to initialize database from schema.sql", ex);
        }
    }
}
