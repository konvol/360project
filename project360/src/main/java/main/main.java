/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author konst
 */
public class main {
    private static final String URL = "jdbc:mysql://";
    private static final String DATABASE = "test";
    private static final int PORT = 3306;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Connection database...");
        return DriverManager.getConnection(URL + ":" + PORT + "/" + DATABASE + "?characterEncoding=UTF-8", USERNAME, PASSWORD);
    }

    public static String getUserName() {
        return USERNAME;
    }

    public static void main(String args[]) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        try {
            con = getConnection();
            stmt = con.createStatement();
            String createEmployee = new String("SELECT * FROM EMPLOYEE WHERE 1");
            stmt.execute(createEmployee);
            ResultSet res = stmt.getResultSet();
            while (res.next()) {
                System.out.println("id: " + res.getString("EmpID"));
            }
            System.out.println("Comple");
        } catch (SQLException ex) {
            System.out.println("Malakia");
        }

    }
}
