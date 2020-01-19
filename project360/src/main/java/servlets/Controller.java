/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import Database.DBAdmin;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author konst
 */
@WebServlet(name = "Controller", urlPatterns = {"/Controller"})
public class Controller extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, ClassNotFoundException {

        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        String res = null;
        String r = "";
        System.out.println("\n\n" + action);
        switch (action) {

            case "initAll":
                res = DBAdmin.initAll();
                break;

            case "getEmployees":
                res = DBAdmin.getEmployees();
                break;

            case "initTables":
                ArrayList<Integer> ar = new ArrayList<Integer>();
                ar.add(2);
                res = DBAdmin.addEmployee("Dimitrakopoulos", "Dimitrios", "2020-01-01", "Odos, Odou 12", "GR123456", "Pancretan Bank", "6999998542", "CSD", "Perm_Admin", "", 0, true, 1, ar);
                ar.add(12);
                res += "\n" + DBAdmin.addEmployee("Papadopoulos", "Georgios", "2020-02-01", "Alli Odos, Odou 13", "GR654321", "Piraeus Bank", "6999955234", "Chemistry", "Perm_Edu", "", 0, false, 2, ar);
                ar.add(21);
                res += "\n" + DBAdmin.addEmployee("Kiriakakis", "Kiriakos", "2019-03-01", "Paralli Odos, Odou 14", "GR615243", "Ethniki Bank", "6999952154", "Math", "Temp_Admin", "2020-11-30", 900.50, true, 3, ar);
                ar.remove(2);
                res += "\n" + DBAdmin.addEmployee("Manousakis", "Emmanouil", "2020-01-01", "Spiti, Spitiou 42", "GR999999", "Alpha Bank", "6985542215", "Physics", "Temp_Edu", "2020-06-30", 1200.50, false, 2, ar);
                res += "\n" + DBAdmin.addEmployee("Panagiotakopoulos", "Panagiotis", "2020-01-01", "Irakleiou 200", "GR666", "EuroBank", "6982441158", "CSD", "Perm_Admin", "", 0, true, 0, null);
                break;

            case "addEmployee":
                String firstname = request.getParameter("firstname");
                String lastname = request.getParameter("lastname");
                String beg_date = request.getParameter("beg_date");
                String bank_name = request.getParameter("bank_name");
                String bank_number = request.getParameter("bank_number");
                String phone = request.getParameter("phone");
                String address = request.getParameter("address");
                String department = request.getParameter("department");
                String type = request.getParameter("type");
                boolean married = Boolean.parseBoolean(request.getParameter("married"));
                ArrayList<Integer> children_ages = new ArrayList<Integer>();
                int children_number = Integer.parseInt(request.getParameter("children_number"));
                if (children_number != 0) {
                    for (int i = 0; i < children_number; i++) {
                        children_ages.add(Integer.parseInt(request.getParameter("child" + Integer.toString(i))));
                    }
                } else {
                    children_ages = null;
                }
                String end_date;
                double temp_income;
                if (type.equals("Temp_Admin") || type.equals("Temp_Edu")) {
                    end_date = request.getParameter("end_date");
                    temp_income = Double.parseDouble(request.getParameter("temp_income"));
                } else {
                    end_date = null;
                    temp_income = -1;
                }
                res = DBAdmin.addEmployee(lastname, firstname, beg_date, address, bank_number, bank_name, phone, department, type, end_date, temp_income, married, children_number, children_ages);
                break;

            case "editEmployee":
                int id = Integer.parseInt(request.getParameter("idEdit"));
                firstname = request.getParameter("nameEdit");
                lastname = request.getParameter("surnameEdit");
                bank_name = request.getParameter("bank_nameEdit");
                bank_number = request.getParameter("bank_numberEdit");
                phone = request.getParameter("phoneEdit");
                address = request.getParameter("addressEdit");
                department = request.getParameter("departmentEdit");
                type = request.getParameter("typeEdit");
                String mar = request.getParameter("marriageEdit");
                System.out.println("----------" + mar + "--------------");
                int kidsPrev = Integer.parseInt(request.getParameter("previousKids"));
                int kidsCurr = Integer.parseInt(request.getParameter("kids_numberEdit"));
                ArrayList<Integer> childrenPrev = new ArrayList<Integer>();
                ArrayList<Integer> childrenNew = new ArrayList<Integer>();
                ArrayList<Integer> childrenIDs = new ArrayList<Integer>();
                for (int i = 0; i < kidsPrev; i++) {
                    childrenPrev.add(Integer.parseInt(request.getParameter("child" + Integer.toString(i) + "Edit")));
                    childrenIDs.add(Integer.parseInt(request.getParameter("childID" + Integer.toString(i) + "Edit")));
                }
                for (int i = 0; i < kidsCurr - kidsPrev; i++) {
                    childrenNew.add(Integer.parseInt(request.getParameter("newChild" + Integer.toString(i) + "Edit")));
                }
                ArrayList<Integer> children = new ArrayList();
                children.addAll(childrenPrev);
                children.addAll(childrenNew);
                res = DBAdmin.editEmployee(id, lastname, firstname, address, bank_number, bank_name, phone, type, children, mar, department);
                res += DBAdmin.editChildren(id, childrenIDs, childrenPrev);
                res += DBAdmin.addChildren(id, childrenNew);
                break;

            case "getEmployeeFull":
                id = Integer.parseInt(request.getParameter("id"));
                res = DBAdmin.getEmployeeFull(id);
                break;

            case "truncate":
                res = DBAdmin.truncateAll();
                break;

            case "updateIncome":
                type = request.getParameter("type");
                double new_income = Double.parseDouble(request.getParameter("income"));
                res = DBAdmin.setBasicIncome(type, new_income);
                break;

            case "currentMonthPayroll":
                String year = request.getParameter("year");
                String month = request.getParameter("month");
                res = DBAdmin.monthPayroll(year, month);
                break;
            case "getCurrentMonthPayroll":
                year = request.getParameter("year");
                month = request.getParameter("month");
                res = DBAdmin.getPayroll(DBAdmin.getLastDay(year, month));
                break;
            case "manyMonthsPayroll":
                String yearFrom = request.getParameter("yearFrom");
                String monthFrom = request.getParameter("monthFrom");
                String yearTo = request.getParameter("yearTo");
                String monthTo = request.getParameter("monthTo");
                System.out.println("From: " + monthFrom + "-" + yearFrom + " To: " + monthTo + "-" + yearTo);
                res = "";
                int cond = 0;
                for (int i = Integer.parseInt(yearFrom); i < Integer.parseInt(yearTo) + 1; i++) {
                    if (i == Integer.parseInt(yearTo)) {
                        cond = (Integer.parseInt(monthTo) + 1);
                    } else {
                        cond = 13;
                    }
                    for (int j = Integer.parseInt(monthFrom); (j < cond); j++) {
                        System.out.println(i + "-" + j);
                        res += DBAdmin.monthPayroll(Integer.toString(i), Integer.toString(j));
                        res += "\n";
                        System.out.println(">>>>>>>>>>>Result:" + res);
                        if (j == 12) {
                            break;
                        }
                    }
                }
                break;
            case "getManyMonthsPayroll":
                yearFrom = request.getParameter("yearFrom");
                monthFrom = request.getParameter("monthFrom");
                yearTo = request.getParameter("yearTo");
                monthTo = request.getParameter("monthTo");
                System.out.println("From: " + monthFrom + "-" + yearFrom + " To: " + monthTo + "-" + yearTo);
                res = "";
                cond = 0;
                for (int i = Integer.parseInt(yearFrom); i < Integer.parseInt(yearTo) + 1; i++) {
                    if (i == Integer.parseInt(yearTo)) {
                        cond = (Integer.parseInt(monthTo) + 1);
                    } else {
                        cond = 13;
                    }
                    for (int j = Integer.parseInt(monthFrom); (j < cond); j++) {
                        System.out.println(i + "-" + j);
                        res += DBAdmin.getPayroll(DBAdmin.getLastDay(Integer.toString(i), Integer.toString(j)));
                        res += "<hr>";
                        System.out.println(">>>>>>>>>>>Result:" + res);
                        if (j == 12) {
                            break;
                        }
                    }
                }
                break;
            case "tempPromotion":
                id = Integer.parseInt(request.getParameter("id"));
                res = DBAdmin.promoteEmployee(id);
                break;

            case "retirement":
                id = Integer.parseInt(request.getParameter("id"));
                String cause = request.getParameter("cause");
                res = DBAdmin.retireEmployee(id, cause);
                break;

            case "getEmployee":
                id = Integer.parseInt(request.getParameter("id"));
                res = DBAdmin.getEmployee(id) + "<br>";
                res += DBAdmin.getFamily(id);
                res += DBAdmin.getKids(id);
                res += DBAdmin.getPay(id, "2020", "01");
                break;

            case "statistics":
                res = DBAdmin.statistics("2020", "01");
                break;

            case "payrollPerType":
                res = DBAdmin.payrollPerType("2020", "01");
                break;
            default:
                res = "Not valid instruction";
                break;
        }

        try ( PrintWriter out = response.getWriter()) {
            out.print(res);
            out.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
