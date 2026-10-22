package jdbc_connection;

import java.sql.*;

public class Jdbc_connection {
    public static void main(String[] args) {
        
        // 1. Connect to MySQL Database
        String url = "jdbc:mysql://localhost:3306/";
        String username = "root"; 
        String password = "your_password"; // CHANGE THIS TO YOUR MYSQL PASSWORD

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();
            
            // Create Database and Table
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS jpadb1");
            statement.execute("USE StudentsDB");
            statement.execute("CREATE TABLE IF NOT EXISTS students (id INT PRIMARY KEY, firstname VARCHAR(255), lastname VARCHAR(255), grade INT)");
            System.out.println("1. Connected to MySQL, Database & Table Created.");

            // 2. Insert Data
            PreparedStatement insertStmt = connection.prepareStatement("INSERT IGNORE INTO students (id, firstname, lastname, grade) VALUES (?, ?, ?, ?)");
            
            // Inserting a single row
            insertStmt.setInt(1, 1); insertStmt.setString(2, "Nabon"); insertStmt.setString(3, "Tefera"); insertStmt.setInt(4, 95);
            insertStmt.executeUpdate();

            // Inserting 10 more rows
            String[][] extraStudents = {
                {"2", "Abebe", "Kebede", "88"}, {"3", "Chala", "Tolosa", "75"},
                {"4", "Sara", "Girma", "92"}, {"5", "Mekdes", "Alemu", "81"},
                {"6", "Dawit", "Tadesse", "79"}, {"7", "Hanna", "Bekele", "96"},
                {"8", "Yosef", "Hailu", "85"}, {"9", "Betelhem", "Assefa", "90"},
                {"10", "Elias", "Worku", "72"}, {"11", "Tigist", "Mulugeta", "89"}
            };
            for (String[] s : extraStudents) {
                insertStmt.setInt(1, Integer.parseInt(s[0]));
                insertStmt.setString(2, s[1]);
                insertStmt.setString(3, s[2]);
                insertStmt.setInt(4, Integer.parseInt(s[3]));
                insertStmt.executeUpdate();
            }
            System.out.println("2. Data inserted successfully.");

            // 3. Retrieve Data (Display 5 rows)
            ResultSet rs = statement.executeQuery("SELECT * FROM students LIMIT 5");
            System.out.println("\n3. First 5 Students:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Name: " + rs.getString("firstname") + " " + rs.getString("lastname") + " | Grade: " + rs.getInt("grade"));
            }

            // 4. Update Data (Update firstname of id 1)
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE students SET firstname = ? WHERE id = ?");
            updateStmt.setString(1, "UpdatedNabon");
            updateStmt.setInt(2, 1);
            updateStmt.executeUpdate();
            System.out.println("\n4. Student ID 1 name updated.");

            // 5. Delete Data (Delete id 2)
            PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM students WHERE id = ?");
            deleteStmt.setInt(1, 2);
            deleteStmt.executeUpdate();
            System.out.println("5. Student ID 2 deleted.");

            // 6. Calculate Average Grade
            ResultSet avgRs = statement.executeQuery("SELECT AVG(grade) AS average_grade FROM students");
            if (avgRs.next()) {
                System.out.println("\n6. Average Grade: " + avgRs.getDouble("average_grade"));
            }

            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}