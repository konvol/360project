/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author konst
 */
public class DBAdmin {
    private static final String URL = "jdbc:mysql://";
    private static final String DATABASE = "hy360";
    private static final int PORT = 3306;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static int currID = 0;
    private static int contractID = 0;
    private static int kidID = 0;
    private static double PermAdminIncome = 1200;
    private static double PermEduIncome = 1100;
    private static double ResearchBonus = 250;
    private static double LibraryBonus = 160;
    private static String depAddr1 = "voutwn 1,Irakleio";
    private static String depAddr2 = "voutwn 2,Irakleio";
    private static String depAddr3 = "voutwn 3,Irakleio";
    private static String depAddr4 = "voutwn 4,Irakleio";
    private static ArrayList<String> payrolls = new ArrayList();
    private static double[] totalIncomes = new double[4];
    private static double[] totalEmpByType = new double[4];
    private static double[] min = new double[4];
    private static double[] max = new double[4];
    private static double[] avg = new double[4];


    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Connection to database...");
        return DriverManager.getConnection(URL + ":" + PORT + "/" + DATABASE + "?characterEncoding=UTF-8", USERNAME, PASSWORD);
    }

    public static String getEmployees() throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query = "SELECT * FROM EMPLOYEE";
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM EMPLOYEE WHERE 1");
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>All Employees</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>First Name</th><th>Last Name</th><th>Beggining Date</th><th>Address</th><th>Bank Number</th><th>Bank Name</th><th>Phone</th><th>Department</th><th>Type</th><th>Family Bonus</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Firstname") + "</td>";
                response += "<td> " + res.getString("Lastname") + "</td>";
                response += "<td>" + res.getDate("Beg_date") + "</td>";
                response += "<td>" + res.getString("Addr") + "</td>";
                response += "<td>" + res.getString("Bank_number") + "</td>";
                response += "<td>" + res.getString("Bank_name") + "</td>";
                response += "<td>" + res.getString("Phone") + "</td>";
                response += "<td>" + res.getString("Department_name") + "</td>";
                response += "<td>" + res.getString("Emp_type") + "</td>";
                response += "<td>" + res.getString("Family_Bonus") + "</td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting users";
            System.out.println("Error");
        }
        return response;
    }

    public static String initAll() throws IOException, ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        List<String> s;
        query = readFile("C:\\Users\\konst\\Desktop\\csd\\year3\\CurrentSemester\\hy360\\Assignments\\project360\\src\\main\\java\\sql\\init.sql");
        s = Arrays.asList(query.split(";"));
        System.out.println(s.get(2));
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            for (int i = 0; i < s.size() - 1; i++) {
                query = s.get(i);
                System.out.println(query);
                stmt.execute(query);
            }
            response = "DATABASE CREATION:DONE";
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "DATABASE CREATION:FAILED";
            System.out.println("Fail");
        }
        return response;
    }

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    public static String addEmployee(String lastname, String firstname, String beg_date, String address, String bank_number, String bank_name, String phone, String department, String type, String end_date, double temp_income, boolean married, int children_number, ArrayList<Integer> children_ages) throws ClassNotFoundException {
        currID++;
        StringBuilder sb = new StringBuilder();
        String values;
        double basic_income = 0;
        double family_bonus;
        String addr = null;
        switch (type) {
            case "Perm_Admin":
                basic_income = PermAdminIncome;
                break;
            case "Temp_Admin":
                basic_income = temp_income;
                break;
            case "Perm_Edu":
                basic_income = PermEduIncome;
                break;
            case "Temp_Edu":
                basic_income = temp_income;
                break;
        }
        switch (department) {
            case "CSD":
                addr = depAddr1;
                break;
            case "Math":
                addr = depAddr2;
                break;
            case "Chemistry":
                addr = depAddr3;
                break;
            case "Physics":
                addr = depAddr4;
                break;
        }
        family_bonus = basic_income * calculateFamilyBonus(married, children_ages, children_number);

        sb.append("('").append(currID).append("',");
        sb.append("'").append(lastname).append("',");
        sb.append("'").append(firstname).append("',");
        sb.append("'").append(beg_date).append("',");
        sb.append("'").append(address).append("',");
        sb.append("'").append(bank_number).append("',");
        sb.append("'").append(bank_name).append("',");
        sb.append("'").append(phone).append("',");
        sb.append("'").append(department).append("',");//.append("',");
        sb.append("'").append(type).append("',");//.append("',");
        sb.append("'").append(family_bonus).append("')");//.append("',");
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO employee(`EmpID`, `Lastname`, `Firstname`, `Beg_date`, `Addr`, `Bank_number`, `Bank_name`, `Phone`, `Department_name`,`Emp_type`, `Family_bonus`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            System.out.println("Done");
            response = "Employee added.<br>";
        } catch (SQLException ex) {
            response = "Error adding Employee.<br>";
            System.out.println("Error");
        }
        response += setFamilyStatus(currID, married, children_number, children_ages);
        switch (type) {
            case "Perm_Admin":
                response += addPermAdmin(currID);
                break;
            case "Temp_Admin":
                response += newContract(currID, type, beg_date, end_date, temp_income);
                response += addTempAdmin(currID, beg_date, end_date);
                break;
            case "Perm_Edu":
                response += addPermEdu(currID);
                break;
            case "Temp_Edu":
                response += newContract(currID, type, beg_date, end_date, temp_income);
                response += addTempEdu(currID, beg_date, end_date);
                break;
        }
        response += addDepartment(currID, addr, department);
        /**/
        return response;

    }

    private static String addDepartment(int id, String address, String department) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String values;
        sb.append("('").append(department).append("',");
        sb.append("'").append(address).append("',");
        sb.append("'").append(id).append("')");//.append("',");
        values = sb.toString();
        System.out.println(values);
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO `department`(`Department_name`, `Addr`,`EmpID`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "Added Employee's department!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "Error adding Employee's department!<br>";
            System.out.println("Error");
        }

        return response;
    }

    private static String addPermAdmin(int ID) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String values;
        sb.append("('").append(ID).append("',");
        sb.append("'").append(PermAdminIncome).append("')");//.append("',");
        values = sb.toString();
        System.out.println(values);
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO `perm_admin`(`EmpID`, `Basic_income`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "Added Permanent Administrative Employee!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "Error adding Permanent Administrative Employee!<br>";
            System.out.println("Error");
        }

        return response;
    }

    private static String addPermEdu(int ID) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String values;
        sb.append("('").append(ID).append("',");
        sb.append("'").append(PermEduIncome).append("',");
        sb.append("'").append(ResearchBonus).append("')");
        values = sb.toString();

        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO `perm_teaching`(`EmpID`, `Basic_income`, `Research_bonus`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "Added Permanent Administrative Employee!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "Error adding Permanent Administrative Employee!<br>";
            System.out.println("Error");
        }
        return response;
    }

    private static String addTempAdmin(int ID, String beg_date, String end_date) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String values;
        sb.append("('").append(ID).append("',");
        sb.append("'").append(end_date).append("')");
        values = sb.toString();
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO `Temp_admin`(`EmpID`, `End_date`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "Added Temporary Administrative Employee!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "Error adding Temporary Administrative Employee!<br>";
            System.out.println("Error");
        }

        return response;
    }

    private static String addTempEdu(int ID, String beg_date, String end_date) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String values;
        sb.append("('").append(ID).append("',");
        sb.append("'").append(LibraryBonus).append("',");
        sb.append("'").append(end_date).append("')");
        values = sb.toString();
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO temp_teaching(`EmpID`, `Library_bonus`, `End_date`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "Added Temporary Educative Employee!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "Error adding Temporary Educative Employee!<br>";
            System.out.println("Error");
        }
        return response;
    }

    private static String setFamilyStatus(int id, boolean married, int children_number, ArrayList<Integer> children_ages) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        sb.append("('").append(id).append("',");
        if (married) {
            sb.append("'").append(1).append("',");
        } else {
            sb.append("'").append(0).append("',");
        }
        sb.append("'").append(children_number).append("')");
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO family_status(`EmpID`, `Marriage_status`, `kids_number`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "Family Status is set!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "Family Status Not set!<br>";
            System.out.println("Error");
        }
        if (children_number > 0) {
            response += setChildren(id, children_ages);
        }
        return response;
    }

    private static String newContract(int currID, String type, String beg_date, String end_date, double income) throws ClassNotFoundException {
        DBAdmin.contractID++;
        StringBuilder sb = new StringBuilder();
        sb.append("('").append(DBAdmin.contractID).append("',");
        sb.append("'").append(currID).append("',");
        sb.append("'").append(beg_date).append("',");
        sb.append("'").append(end_date).append("',");
        sb.append("'").append(income).append("')");
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO temp_contract(`ContractID`, `EmpID`, `Beg_date`,`End_date`,`Basic_income`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "New Contract Is Signed!<br>";
            System.out.println("Done");
        } catch (SQLException ex) {
            response = "New Contract Is Not Signed!<br>";
            System.out.println("Error");
        }
        return response;

    }


    public static String getEmployeeFull(int id) throws ClassNotFoundException, SQLException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM EMPLOYEE WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Requested Employee</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>First Name</th><th>Last Name</th>" +/*<th>Beggining Date</th>*/ "<th>Address</th><th>Bank Number</th><th>Bank Name</th><th>Phone</th><th>Department</th><th>Type</th>";//<th>Family Bonus</th><th>Type</th></tr>";
            res.next();
            response += "<tr>";
            response += "<td id='idEdit'>" + res.getString("EmpID") + "</td>";
            response += "<td><input id='nameEdit' type='text' size='15' value='" + res.getString("Firstname") + "'></td>";
            response += "<td><input id='surnameEdit' type='text' size='15' value='" + res.getString("Lastname") + "'></td>";
            response += "<td><input id='addressEdit' type='text' size='20' value='" + res.getString("Addr") + "'></td>";
            response += "<td><input id='bank_numberEdit' type='text' size='10' value='" + res.getString("Bank_number") + "'></td>";
            response += "<td><input id='bank_nameEdit' type='text' size='10' value='" + res.getString("Bank_name") + "'></td>";
            response += "<td><input id='phoneEdit' type='text' size='10' value='" + res.getString("Phone") + "'></td>";
            response += "<td><input id='department_nameEdit' type='text' size='10' value='" + res.getString("Department_name") + "'></td>";
            response += "<td id='typeEdit'>" + res.getString("Emp_type") + "</td>";
            response += "</tr>";
            response += "</table></div><br>";
            response += getFamilyStatus(id);
            response += "<div id='adviceFamilyStatus'> Don't type less children than you already have.</div>";
            response += getChildren(id);
            response += "<div id='newChildren'> </div>";
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String getEmployee(int id) throws ClassNotFoundException, SQLException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        String type;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM EMPLOYEE WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Requested Employee</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>First Name</th><th>Last Name</th>" +/*<th>Beggining Date</th>*/ "<th>Address</th><th>Bank Number</th><th>Bank Name</th><th>Phone</th><th>Department</th><th>Type</th>";//<th>Family Bonus</th><th>Type</th></tr>";
            res.next();
            response += "<tr>";
            response += "<td id='idEdit'>" + res.getString("EmpID") + "</td>";
            response += "<td>" + res.getString("Firstname") + "</td>";
            response += "<td>" + res.getString("Lastname") + "</td>";
            response += "<td>" + res.getString("Addr") + "</td>";
            response += "<td>" + res.getString("Bank_number") + "</td>";
            response += "<td>" + res.getString("Bank_name") + "</td>";
            response += "<td>" + res.getString("Phone") + "</td>";
            response += "<td>" + res.getString("Department_name") + "</td>";
            response += "<td>" + res.getString("Emp_type") + "</td>";
            response += "</tr>";
            response += "</table></div><br>";
            type = res.getString("Emp_type");
            switch (type) {
                case "Perm_Admin":
                    response += DBAdmin.getPermAdmin(id);
                    break;
                case "Perm_Edu":
                    response += DBAdmin.getPermEdu(id);
                    break;
                case "Temp_Admin":
                    response += DBAdmin.getTempAdmin(id);
                    break;
                case "Temp_Edu":
                    response += DBAdmin.getTempEdu(id);
                    break;
            }
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String getPermAdmin(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("permadmin");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM Perm_admin WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Employee Type Details:</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>Basic Income</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Basic_income") + "</td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple PermAdmin");
        } catch (SQLException ex) {
            response = "Error getting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String getPermEdu(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("permedu");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM Perm_teaching WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Employee Type Details:</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>Basic Income</th><th>Research Bonus</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Basic_income") + "</td>";
                response += "<td>" + res.getString("Research_bonus") + "</td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple PermAdmin");
        } catch (SQLException ex) {
            response = "Error getting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String getTempAdmin(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("tempadmin");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM Temp_admin WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Employee Type Details:</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>End Date</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("End_date") + "</td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple PermEdu");
        } catch (SQLException ex) {
            response = "Error getting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String getTempEdu(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("tempedu");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM Temp_teaching WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Employee Type Details:</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>Library Bonus</th><th>End Date</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Library_bonus") + "</td>";
                response += "<td>" + res.getString("End_date") + "</td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple TempEdu");
        } catch (SQLException ex) {
            response = "Error getting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String getFamily(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("familyStatus");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM family_status WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Employee Family Satus Details:</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>Marriage Status</th><th>Kids Number</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                String s = res.getString("Marriage_status");
                if (s.equals(Integer.toString(1))) {
                    s = "Yes";
                } else {
                    s = "No";
                }
                response += "<td>" + s + "</td>";
                response += "<td>" + res.getString("kids_number") + "</td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple FamStatus");
        } catch (SQLException ex) {
            response = "Error getting FamStatus";
            System.out.println("Error");
        }
        return response;
    }

    public static String getKids(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        int i = 0;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM Kids WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<br><div><label><span><b>Employee's Kids Details:</b></span></label><br>";
            response += "<table><tr><th>Kid ID</th><th>Employee ID</th><th>Age</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td id='childID" + i + "Edit'>" + res.getString("KidID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Age") + "</td>";
                response += "</tr>";
                i++;
            }
            response += "</table></div><br>";
            System.out.println("Comple Kids");

        } catch (SQLException ex) {
            response = "Error getting employee's kids";
            System.out.println("Error");
        }
        return response;
    }

    public static String getPay(int id, String year, String month) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        String pay_date = DBAdmin.getLastDay(year, month);
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM payroll WHERE Payment_date='" + pay_date + "' && EmpId=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Payment For " + pay_date + " for Employee. </b></span></label><br>";
            response += "<table><tr><th>Payroll ID</th><th>Employee ID</th><th>Basic Income</th><th>Family Bonus</th><th>Educational Bonus</th><th>Total</th><th>Payment Date</th><th>Bank Number</th><th>Bank Name</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("PayrollID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Basic_income") + "</td>";
                response += "<td> " + res.getString("Family_bonus") + "</td>";
                response += "<td>" + res.getString("Teaching_bonus") + "</td>";
                response += "<td>" + res.getString("Total") + "</td>";
                response += "<td>" + res.getDate("Payment_date") + "</td>";
                response += "<td>" + res.getString("Bank_number") + "</td>";
                response += "<td>" + res.getString("Bank_name") + "</td>";
                response += "</tr>";
            }
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting payroll";
            System.out.println("Error");
        }
        response += "</table></div><br>";
        return response;
    }

    private static String getFamilyStatus(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("familyStatus");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM family_status WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Employee Family Satus Details:</b></span></label><br>";
            response += "<table><tr><th>Employee ID</th><th>Marriage Status</th><th>Kids Number</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                String s = res.getString("Marriage_status");
                if (s.equals(Integer.toString(1))) {
                    s = "Yes";
                } else {
                    s = "No";
                }
                response += "<td><input id='marriageEdit' type='text' size='15' value='" + s + "'></td>";
                response += "<td><input id='kids_numberEdit' onkeyup='extraKids()' type='text' size='15' value='" + res.getString("kids_number") + "'></td>";
                response += "</tr>";
            }
            response += "</table></div>";
            System.out.println("Comple FamStatus");
        } catch (SQLException ex) {
            response = "Error getting FamStatus";
            System.out.println("Error");
        }
        return response;
    }

    private static String getChildren(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response;
        String query;
        System.out.println("children");
        int i = 0;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM Kids WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<br><div><label><span><b>Employee's Kids Details:</b></span></label><br>";
            response += "<table><tr><th>Kid ID</th><th>Employee ID</th><th>Age</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td id='childID" + i + "Edit'>" + res.getString("KidID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td><input id='child" + i + "Edit' type='text' size='15' value='" + res.getString("Age") + "'></td>";
                response += "</tr>";
                i++;
            }
            response += "</table></div><br>";
            System.out.println("Comple Kids");

        } catch (SQLException ex) {
            response = "Error getting employee's kids";
            System.out.println("Error");
        }
        return response;

    }

    private static double calculateFamilyBonus(boolean married, ArrayList<Integer> children_ages, int children_number) {
        double bonus = 0;
        if (married) {
            bonus += 0.05;
        }
        for (int i = 0; i < children_number; i++) {
            if (children_ages.get(i) < 18) {
                bonus += 0.05;
            }
        }
        return bonus;
    }

    private static String setChildren(int id, ArrayList<Integer> children_ages) throws ClassNotFoundException {
        String response = "";
        if (children_ages.size() == 0) {
            return "No Children to add.";
        } else {
            int currChild;
            for (int i = 0; i < children_ages.size(); i++) {
                kidID++;
                currChild = children_ages.get(i);
                StringBuilder sb = new StringBuilder();
                sb.append("('").append(kidID).append("',");
                sb.append("'").append(id).append("',");
                sb.append("'").append(currChild).append("')");
                Statement stmt = null;
                Connection con = null;
                String query;
                try {
                    con = DBAdmin.getConnection();
                    stmt = con.createStatement();
                    query = new String("INSERT INTO kids(`KidID`,`EmpID`, `Age`) VALUES" + sb.toString());
                    System.out.println(query);
                    stmt.execute(query);
                    ResultSet res = stmt.getResultSet();
                    response += "Kid " + Integer.toString((1 + i)) + " is set!<br>";
                    System.out.println("Done");
                } catch (SQLException ex) {
                    response += "Kid " + Integer.toString((1 + i)) + " not set!<br>";
                    System.out.println("Error");
                }
            }
        }
        return response;
    }
    
    public static String editEmployee(int id,
            String lastname,
            String firstname,
            String address,
            String bank_number,
            String bank_name,
            String phone,
            String type,
            ArrayList<Integer> children_ages,
            String married,
            String department) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        double family_bonus = 0;
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = new String("");

        System.out.println("----------" + married + "--------------");
        boolean mar;
        if (married.equals("true")) {
            mar = true;
        } else {
            mar = false;
        }

        System.out.println("----------" + mar + "--------------");
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            double income = 0;
            switch (type) {
                case "Perm_Admin":
                    query = "SELECT basic_income FROM perm_admin WHERE EmpID='" + id + "'";
                    break;
                case "Temp_Admin":
                    query = "SELECT basic_income FROM temp_contract WHERE EmpID='" + id + "'";
                    break;
                case "Perm_Edu":
                    query = "SELECT basic_income FROM perm_teaching WHERE EmpID='" + id + "'";
                    break;
                case "Temp_Edu":
                    query = "SELECT basic_income FROM temp_contract WHERE EmpID='" + id + "'";
                    break;
            }
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            res.next();

            income = Double.parseDouble(res.getString("basic_income"));
            family_bonus = income * DBAdmin.calculateFamilyBonus(mar, children_ages, children_ages.size());
            System.out.println("income:" + income + " bonus:" + family_bonus);
            query = "UPDATE employee\nSET firstname='" + firstname + "',";
            query += "lastname='" + lastname + "',";
            query += "addr='" + address + "',";
            query += "bank_number='" + bank_number + "',";
            query += "bank_name='" + bank_name + "',";
            query += "phone='" + phone + "',";
            query += "department_name='" + department + "',";
            query += "family_bonus='" + family_bonus + "',";
            query += "bank_name='" + bank_name + "'\n";
            query += "WHERE EmpID='" + id + "'";
            System.out.println(query);
            stmt.execute(query);

            query = "UPDATE family_status\nSET kids_number='" + children_ages.size() + "',";
            if (mar) {
                query += "marriage_status='1'\n";
            } else {
                query += "marriage_status='0'\n";
            }
            query += "WHERE EmpID='" + id + "'";
            System.out.println(query);
            stmt.execute(query);

            query = "UPDATE department\nSET department_name='" + department + "'\n";
            query += "WHERE EmpID='" + id + "'";
            System.out.println(query);
            stmt.execute(query);

            response = "Employee updated. <br>Family status updated. (If changed)<br>";
        } catch (SQLException ex) {
            response = "Error updating Employee.<br>";
            System.out.println("Error");
        }
        return response;
    }

    public static String editChildren(int id, ArrayList<Integer> childrenIDs, ArrayList<Integer> childrenNew) throws ClassNotFoundException {
        String response = "";
        if (childrenNew.size() == 0) {
            return "No Children to add.";
        } else {
            int currChild;
            for (int i = 0; i < childrenNew.size(); i++) {
                currChild = childrenNew.get(i);
                Statement stmt = null;
                Connection con = null;
                String query;
                query = "UPDATE kids\nSET age='" + currChild + "'\n" + "WHERE KidID='" + childrenIDs.get(i) + "'";
                System.out.println(query);
                try {
                    con = DBAdmin.getConnection();
                    stmt = con.createStatement();
                    stmt.execute(query);
                    ResultSet res = stmt.getResultSet();
                    response += "Kid with id " + childrenIDs.get(i) + " is updated!<br>";
                    System.out.println("Done");
                } catch (SQLException ex) {
                    response += "Kid with id " + childrenIDs.get(i) + " not updated!<br>";
                    System.out.println("Error");
                }
            }
        }
        return response;
    }

    public static String addChildren(int id, ArrayList<Integer> childrenPrev) throws ClassNotFoundException {
        String response = "";

        if (childrenPrev.size() == 0) {
            return "No Children to add.";
        } else {
            int currChild;
            for (int i = 0; i < childrenPrev.size(); i++) {
                currChild = childrenPrev.get(i);
                kidID++;
                StringBuilder sb = new StringBuilder();
                sb.append("('").append(kidID).append("',");
                sb.append("'").append(id).append("',");
                sb.append("'").append(currChild).append("')");
                Statement stmt = null;
                Connection con = null;
                String query;
                try {
                    con = DBAdmin.getConnection();
                    stmt = con.createStatement();
                    query = new String("INSERT INTO kids(`KidID`,`EmpID`, `Age`) VALUES" + sb.toString());
                    System.out.println(query);
                    stmt.execute(query);
                    ResultSet res = stmt.getResultSet();
                    response += "New Kid " + Integer.toString((1 + i)) + " is added!<br>";
                    System.out.println("Done");
                } catch (SQLException ex) {
                    response += "New Kid " + Integer.toString((1 + i)) + " not added!<br>";
                    System.out.println("Error");
                }
            }
        }
        return response;
    }

    public static String truncateAll() throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("TRUNCATE TABLE hy360.employee;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.department;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.family_status;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.kids;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.payroll;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.perm_admin;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.perm_teaching;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.temp_teaching;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.temp_admin;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            query = new String("TRUNCATE TABLE hy360.temp_contract;\n");
            System.out.println(query);
            stmt.executeLargeUpdate(query);
            response = "Success! All tables truncated.";
            System.out.println("Success");
        } catch (SQLException ex) {
            response = "Error! Tables not truncated.";
            System.out.println("Error");
        }
        return response;
    }

    public static String setBasicIncome(String type, double newIncome) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = null;
        switch (type) {
            case "Perm_Edu":
                if (newIncome > PermEduIncome) {
                    PermEduIncome = newIncome;
                    query = "UPDATE perm_teaching SET Basic_income=" + PermEduIncome + " WHERE 1";
                    response += "Permanent Educative Basic Income has changed.";
                } else {
                    response += "Permanent Educative Basic Income could not be changed.\nNew income is not valid.";
                }
                break;
            case "Perm_Admin":
                if (newIncome > PermAdminIncome) {
                    PermAdminIncome = newIncome;
                    query = "UPDATE perm_admin SET Basic_income=" + PermAdminIncome + " WHERE 1";
                    response += "Permanent Administrative Basic Income has changed.";
                } else {
                    response += "Permanent Administrative Basic Income could not be changed.\nNew income is not valid.";
                }
                break;
            case "LibraryBonus":
                if (newIncome > LibraryBonus) {
                    LibraryBonus = newIncome;
                    query = "UPDATE temp_teaching SET library_income=" + LibraryBonus + " WHERE 1";
                    response += "Library Bonus has changed.";
                } else {
                    response += "Library Bonus could not be changed.\nNew value is not valid.";
                }
                break;
            case "ResearchBonus":
                if (newIncome > ResearchBonus) {
                    ResearchBonus = newIncome;
                    query = "UPDATE perm_teaching SET research_bonus=" + ResearchBonus + " WHERE 1";
                    response += "Research Bonus has changed.";
                } else {
                    response += "Research Bonus could not be changed.\nNew value is not valid.";
                }
                break;
        }
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            stmt.executeLargeUpdate(query);
            System.out.println("Success");
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        return response;
    }

    public static String getLastDay(String year, String month) {
        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.YEAR, Integer.parseInt(year));
        myCal.set(Calendar.MONTH, Integer.parseInt(month));
        myCal.set(Calendar.DAY_OF_MONTH, 1);
        myCal.add(Calendar.DATE, -1);
        long date = myCal.getTimeInMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String pay_date = formatter.format(date);
        return pay_date;
    }
    public static String monthPayroll(String year, String month) throws ClassNotFoundException {
        int id = 0;
        double basic_income = 0;
        double family_bonus = 0;
        String bank_number = null;
        String bank_name = null;
        String beg_date = null;
        String end_date = null;
        String type = null;
        double edu_bonus = 0;
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = null;
        String[] beg = new String[3];
        String[] pay = new String[3];
        String[] end = new String[3];
        ResultSet r1;
        String pay_date;
        pay_date = getLastDay(year, month);
        System.out.println(">>>>>>>>>>payment date: " + pay_date);
        if (!payrolls.contains(pay_date)) {
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            ResultSet res;
            stmt = con.createStatement();
            query = "SELECT * FROM employee WHERE 1";
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            loop:
            while (res.next()) {
                id = 0;
                basic_income = 0;
                family_bonus = 0;
                bank_number = null;
                bank_name = null;
                beg_date = null;
                end_date = null;
                type = null;
                edu_bonus = 0;
                query = null;
                id = Integer.parseInt(res.getString("EmpID"));
                family_bonus = Double.parseDouble(res.getString("Family_bonus"));
                type = res.getString("Emp_type");
                bank_name = res.getString("Bank_name");
                bank_number = res.getString("Bank_number");
                beg_date = res.getString("Beg_date");
                beg = beg_date.split("-");
                pay = pay_date.split("-");
                if (Integer.parseInt(beg[0]) > Integer.parseInt(pay[0])) {
                    System.out.println("Error. Employee signed later than given date(Year)");
                    response += "Error. Employee signed later than given date(Year)";
                    continue loop;
                } else {
                    if (Integer.parseInt(beg[0]) == Integer.parseInt(pay[0]) && Integer.parseInt(beg[1]) > Integer.parseInt(pay[1])) {
                        System.out.println("Error. Employee signed later than given date(Month)");
                        response += "Error. Employee signed later than given date(Month)<br>";
                    } else {
                        switch (type) {
                            case "Perm_Admin":
                                query = "SELECT basic_income FROM perm_admin WHERE EmpID='" + id + "'";
                                stmt = con.createStatement();
                                System.out.println(query);
                                stmt.execute(query);
                                r1 = stmt.getResultSet();
                                r1.next();
                                basic_income = Double.parseDouble(r1.getString("Basic_income"));
                                break;
                            case "Temp_Admin":
                                query = "      SELECT temp_admin.EmpID, temp_contract.Basic_income, temp_contract.End_date\n"
                                        + "    FROM temp_contract\n"
                                        + "    LEFT JOIN temp_admin\n"
                                        + "    ON temp_contract.EmpID = temp_admin.EmpID\n"
                                        + "    WHERE temp_admin.EmpID=\n" + id + "";
                                stmt = con.createStatement();
                                System.out.println(query);
                                stmt.execute(query);
                                r1 = stmt.getResultSet();
                                r1.next();
                                basic_income = Double.parseDouble(r1.getString("Basic_income"));
                                end_date = r1.getString("End_date");
                                end = end_date.split("-");
                                if (Integer.parseInt(end[0]) < Integer.parseInt(pay[0])) {
                                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!Contract has ended!!!!!!!!!!!!!!!!!!!!!");
                                    response += "!!!!!!!!!!!!!!!!!!!!!!Contract has ended!!!!!!!!!!!!!!!!!!!!!";
                                    continue loop;
                                } else {
                                    if (Integer.parseInt(end[1]) < Integer.parseInt(pay[1])) {
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!Contract has ended!!!!!!!!!!!!!!!!!!!!!");
                                        response += "!!!!!!!!!!!!!!!!!!!!!!Contract has ended!!!!!!!!!!!!!!!!!!!!!";
                                        continue loop;
                                    }
                                }
                                break;
                            case "Perm_Edu":
                                query = "SELECT basic_income,research_bonus FROM perm_teaching WHERE EmpID='" + id + "'";
                                stmt = con.createStatement();
                                System.out.println(query);
                                stmt.execute(query);
                                r1 = stmt.getResultSet();
                                r1.next();
                                basic_income = Double.parseDouble(r1.getString("Basic_income"));
                                edu_bonus = Double.parseDouble(r1.getString("Research_bonus"));
                                break;
                            case "Temp_Edu":
                                query = "      SELECT temp_teaching.EmpID,temp_contract.Basic_income,temp_contract.End_date,temp_teaching.Library_bonus\n"
                                        + "    FROM temp_contract\n"
                                        + "    LEFT JOIN temp_teaching\n"
                                        + "    ON temp_contract.EmpID = temp_teaching.EmpID\n"
                                        + "    WHERE temp_teaching.EmpID=" + id + "";
                                stmt = con.createStatement();
                                System.out.println(query);
                                stmt.execute(query);
                                r1 = stmt.getResultSet();
                                r1.next();
                                basic_income = Double.parseDouble(r1.getString("Basic_income"));
                                end_date = r1.getString("End_date");
                                end = end_date.split("-");
                                if (Integer.parseInt(end[0]) < Integer.parseInt(pay[0])) {
                                    System.out.println("id:" + id + " !!!!!!!!!!!!!!!!!!!!!!Contract has ended!!!!!!!!!!!!!!!!!!!!!<br>");
                                    continue loop;
                                } else {
                                    if (Integer.parseInt(end[1]) < Integer.parseInt(pay[1])) {
                                        System.out.println("id:" + id + " !!!!!!!!!!!!!!!!!!Contract has ended!!!!!!!!!!!!!!!!!!!!!!<br>");
                                        continue loop;
                                    }
                                }
                                edu_bonus = Double.parseDouble(r1.getString("Library_bonus"));
                                break;
                        }
                        response += DBAdmin.newPayroll(id, basic_income, family_bonus, edu_bonus, beg_date, bank_number, bank_name, type, pay_date);
                    }

                }
                response += "<br>";
            }
            payrolls.add(pay_date);
            //System.out.println("!!!!!------------!!!!!!!-------" + pay_date + "!!!!!------------!!!!!!!-------");
            System.out.println("Success");
        } catch (SQLException ex) {
            System.out.println("Error1");
            }
        } else {
            response += "Payroll already initialized<br>";
            System.out.println("Error1");
        }

        return response;
    }

    private static String newPayroll(int id, double basic_income, double family_bonus, double edu_bonus, String beg_date, String bank_number, String bank_name, String type, String pay_date) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        String values;
        double total = basic_income + family_bonus + edu_bonus;
        sb.append("('").append(id).append("',");
        sb.append("'").append(basic_income).append("',");
        sb.append("'").append(family_bonus).append("',");
        sb.append("'").append(edu_bonus).append("',");
        sb.append("'").append(total).append("',");
        sb.append("'").append(pay_date).append("',");
        sb.append("'").append(bank_number).append("',");
        sb.append("'").append(bank_name).append("')");//.append("',");
        values = sb.toString();
        System.out.println(values);
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query;
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("INSERT INTO `payroll`(`EmpID`, `Basic_income`, `Family_bonus`, `Teaching_bonus`, `Total`, `Payment_date`, `Bank_number`, `Bank_name`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response += "New Payroll for employee with id:" + id + " for " + pay_date + " ok!<br>";
        } catch (SQLException ex) {
            response = "Error creating payroll!<br>";
            System.out.println("Error");
        }
        return response;
    }

    public static String getPayroll(String pay_date) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM payroll WHERE Payment_date='" + pay_date + "' ");
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response = "<div><label><span><b>Payment For " + pay_date + " </b></span></label><br>";
            response += "<table><tr><th>Payroll ID</th><th>Employee ID</th><th>Basic Income</th><th>Family Bonus</th><th>Educational Bonus</th><th>Total</th><th>Payment Date</th><th>Bank Number</th><th>Bank Name</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("PayrollID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Basic_income") + "</td>";
                response += "<td> " + res.getString("Family_bonus") + "</td>";
                response += "<td>" + res.getString("Teaching_bonus") + "</td>";
                response += "<td>" + res.getString("Total") + "</td>";
                response += "<td>" + res.getDate("Payment_date") + "</td>";
                response += "<td>" + res.getString("Bank_number") + "</td>";
                response += "<td>" + res.getString("Bank_name") + "</td>";
                response += "</tr>";
            }
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting payroll";
            System.out.println("Error");
        }
        response += "</table></div><br>";
        return response;
    }

    public static String promoteEmployee(int id) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("SELECT * FROM employee WHERE EmpID=" + id + " ");
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            res.next();
            String firstname = res.getString("Firstname");
            String lastname = res.getString("Lastname");
            String address = res.getString("Addr");
            String bank_name = res.getString("Bank_name");
            String bank_number = res.getString("Bank_number");
            String phone = res.getString("Phone");
            String department = res.getString("Department_name");
            String type = res.getString("Emp_type");

            String currEnd = DBAdmin.getLastDay("2020", "01");
            Calendar myCal = Calendar.getInstance();
            String s[] = new String[3];
            s = currEnd.split("-");
            myCal.set(Calendar.YEAR, Integer.parseInt(s[0]));
            myCal.set(Calendar.MONTH, Integer.parseInt(s[1] + 1));
            myCal.set(Calendar.DAY_OF_MONTH, 1);
            long date = myCal.getTimeInMillis();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String newBeg = formatter.format(date);
            String prevType = type;
            double basic_income = 0;
            double family_bonus = Double.parseDouble(res.getString("Family_bonus"));
            currID++;
            switch (type) {
                case "Temp_Admin":
                    type = "Perm_Admin";
                    basic_income = PermAdminIncome;
                    DBAdmin.addPermAdmin(currID);
                    break;
                case "Temp_Edu":
                    type = "Perm_teaching";
                    basic_income = PermEduIncome;
                    DBAdmin.addPermEdu(currID);
                    break;
                default:
                    return "Cannot Promote Permanent Employee. <br> Please Choose a Temporary one! :)";

            }
            StringBuilder sb = new StringBuilder();
            sb.append("('").append(currID).append("',");
            sb.append("'").append(lastname).append("',");
            sb.append("'").append(firstname).append("',");
            sb.append("'").append(newBeg).append("',");
            sb.append("'").append(address).append("',");
            sb.append("'").append(bank_number).append("',");
            sb.append("'").append(bank_name).append("',");
            sb.append("'").append(phone).append("',");
            sb.append("'").append(department).append("',");
            if (type.equals("Perm_teaching")) {
                sb.append("'").append("Perm_Edu").append("',");//.append("',");
            } else {
                sb.append("'").append(type).append("',");//.append("',");
            }
            sb.append("'").append(family_bonus).append("')");

            stmt = con.createStatement();
            query = new String("INSERT INTO employee(`EmpID`, `Lastname`, `Firstname`, `Beg_date`, `Addr`, `Bank_number`, `Bank_name`, `Phone`, `Department_name`,`Emp_type`, `Family_bonus`) VALUES" + sb.toString());
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();

            stmt = con.createStatement();
            query = new String("UPDATE " + prevType + " SET End_date='" + currEnd + "' WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            stmt = con.createStatement();
            query = new String("UPDATE temp_contract SET End_date='" + currEnd + "' WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            stmt = con.createStatement();
            query = new String("UPDATE family_status SET EmpID=" + currID + " WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            stmt = con.createStatement();
            query = new String("UPDATE kids SET EmpID=" + currID + " WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();

            stmt = con.createStatement();
            query = new String("UPDATE department SET EmpID=" + currID + " WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();

            stmt = con.createStatement();
            query = new String("DELETE FROM employee WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();

            System.out.println("Done");
            response = "Employee Promoted.<br>";
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error promoting employee";
            System.out.println("Error");
        }
        return response;
    }

    public static String retireEmployee(int id, String cause) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();
            query = new String("DELETE FROM employee WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            stmt = con.createStatement();
            query = new String("DELETE FROM family_status WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            stmt = con.createStatement();
            query = new String("DELETE FROM kids WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            stmt = con.createStatement();
            query = new String("DELETE FROM department WHERE EmpID=" + id);
            System.out.println(query);
            stmt.execute(query);

            String end_date = DBAdmin.getLastDay("2020", "01");

            stmt = con.createStatement();
            query = new String("DELETE FROM payroll WHERE EmpID=" + id + " && Payment_date>" + end_date);
            System.out.println(query);
            stmt.execute(query);

            response = "Employee with id:" + id + " has been " + cause + ". <br>/*He/she still appears in payrolls until last day of being an employee.*/ <br>We'll miss him/her.. :(";
            System.out.println("Comple");
        } catch (SQLException ex) {
            System.out.println("Error");
        }
        return response;
    }

    public static String statistics(String year, String month) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();

            response += "<h3>Maximum Total Incomes</h3>";
            query = new String("SELECT MAX(payroll.Total) FROM payroll INNER JOIN temp_teaching ON temp_teaching.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            res.next();
            response += "Temporary Educative Max Total Income=" + res.getString("MAX(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT MAX(payroll.Total) FROM payroll INNER JOIN temp_admin ON temp_admin.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Temporary Administrative Max Total Income=" + res.getString("MAX(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT MAX(payroll.Total) FROM payroll INNER JOIN perm_teaching ON perm_teaching.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Permanent Educative Max Total Income=" + res.getString("MAX(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT MAX(payroll.Total) FROM payroll INNER JOIN perm_admin ON perm_admin.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Permanent Administrative Max Total Income=" + res.getString("MAX(payroll.Total)");

            response += "<h3>Minimum Total Incomes</h3>";
            query = new String("SELECT MIN(payroll.Total) FROM payroll INNER JOIN temp_teaching ON temp_teaching.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "Temporary Educative Min Total Income=" + res.getString("MIN(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT MIN(payroll.Total) FROM payroll INNER JOIN temp_admin ON temp_admin.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Temporary Administrative Min Total Income=" + res.getString("MIN(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT MIN(payroll.Total) FROM payroll INNER JOIN perm_teaching ON perm_teaching.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Permanent Educative Min Total Income=" + res.getString("Min(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT MIN(payroll.Total) FROM payroll INNER JOIN perm_admin ON perm_admin.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Permanent Administrative MIN Total Income=" + res.getString("MIN(payroll.Total)");

            response += "<h3>Average Total Incomes</h3>";
            query = new String("SELECT AVG(payroll.Total) FROM payroll INNER JOIN temp_teaching ON temp_teaching.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "Temporary Educative Average Total Income=" + res.getString("AVG(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT AVG(payroll.Total) FROM payroll INNER JOIN temp_admin ON temp_admin.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Temporary Administrative Average Total Income=" + res.getString("AVG(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT AVG(payroll.Total) FROM payroll INNER JOIN perm_teaching ON perm_teaching.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Permanent Educative Average Total Income=" + res.getString("AVG(payroll.Total)");

            stmt = con.createStatement();
            query = new String("SELECT AVG(payroll.Total) FROM payroll INNER JOIN perm_admin ON perm_admin.EmpID=payroll.EmpID");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            res.next();
            response += "<br>Permanent Administrative Average Total Income=" + res.getString("AVG(payroll.Total)");
            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting statistics";
            System.out.println("Error");
        }
        return response;
    }

    public static String payrollPerType(String year, String month) throws ClassNotFoundException {
        String pay_date = DBAdmin.getLastDay(year, month);
        Statement stmt = null;
        Connection con = null;
        String response = "";
        String query = "";
        try {
            con = DBAdmin.getConnection();
            stmt = con.createStatement();

            query = new String("SELECT payroll.payrollID, payroll.EmpID, payroll.Total, payroll.Payment_date FROM payroll INNER JOIN temp_teaching ON temp_teaching.EmpID=payroll.EmpID WHERE payroll.Payment_date='" + pay_date + "'");
            System.out.println(query);
            stmt.execute(query);
            ResultSet res = stmt.getResultSet();
            response += "<div><label><span><b>Temporary Educative Employees Payrolls</b></span></label><br>";
            response += "<table><tr><th>Payroll ID</th><th>Employee ID</th><th>Total</th><th>Payment Date</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("PayrollID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Total") + "</td>";
                response += "<td>" + res.getDate("Payment_date") + "</td>";
                response += "</tr>";
            }
            response += "</table></div><hr>";

            query = new String("SELECT payroll.payrollID, payroll.EmpID, payroll.Total, payroll.Payment_date FROM payroll INNER JOIN perm_teaching ON perm_teaching.EmpID=payroll.EmpID WHERE payroll.Payment_date='" + pay_date + "'");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            response += "<div><label><span><b>Perpament Educative Employees Payrolls</b></span></label><br>";
            response += "<table><tr><th>Payroll ID</th><th>Employee ID</th><th>Total</th><th>Payment Date</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("PayrollID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Total") + "</td>";
                response += "<td>" + res.getDate("Payment_date") + "</td>";
                response += "</tr>";
            }
            response += "</table></div><hr>";

            stmt = con.createStatement();
            query = new String("SELECT payroll.payrollID, payroll.EmpID, payroll.Total, payroll.Payment_date FROM payroll INNER JOIN temp_admin ON temp_admin.EmpID=payroll.EmpID WHERE payroll.Payment_date='" + pay_date + "'");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            response += "<div><label><span><b>Temporary Administrative Employees Payrolls</b></span></label><br>";
            response += "<table><tr><th>Payroll ID</th><th>Employee ID</th><th>Total</th><th>Payment Date</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("PayrollID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Total") + "</td>";
                response += "<td>" + res.getDate("Payment_date") + "</td>";
                response += "</tr>";
            }
            response += "</table></div><hr>";

            stmt = con.createStatement();
            query = new String("SELECT payroll.payrollID, payroll.EmpID, payroll.Total, payroll.Payment_date FROM payroll INNER JOIN perm_admin ON perm_admin.EmpID=payroll.EmpID WHERE payroll.Payment_date='" + pay_date + "'");
            System.out.println(query);
            stmt.execute(query);
            res = stmt.getResultSet();
            response += "<div><label><span><b>Permanent Administrative Employees Payrolls</b></span></label><br>";
            response += "<table><tr><th>Payroll ID</th><th>Employee ID</th><th>Total</th><th>Payment Date</th></tr>";
            while (res.next()) {
                response += "<tr>";
                response += "<td>" + res.getString("PayrollID") + "</td>";
                response += "<td>" + res.getString("EmpID") + "</td>";
                response += "<td>" + res.getString("Total") + "</td>";
                response += "<td>" + res.getDate("Payment_date") + "</td>";
                response += "</tr>";
            }
            response += "</table></div><hr>";

            System.out.println("Comple");
        } catch (SQLException ex) {
            response = "Error getting statistics";
            System.out.println("Error");
        }
        response += "</table></div><br>";
        return response;
    }
}
