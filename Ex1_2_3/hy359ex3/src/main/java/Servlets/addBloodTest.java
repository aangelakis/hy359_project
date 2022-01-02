/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import database.tables.EditBloodTestTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.BloodTest;
import mainClasses.JSON_Converter;

/**
 *
 * @author alex
 */
public class addBloodTest extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
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

        LocalDate date_now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Gson gson = new Gson();

        JSON_Converter jc = new JSON_Converter();

        String test = jc.getJSONFromAjax(request.getReader());

        BloodTest bt = gson.fromJson(test, BloodTest.class);
        LocalDate bt_date = LocalDate.parse(bt.getTest_date(), formatter);
        EditBloodTestTable ebtt = new EditBloodTestTable();

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (bt_date.compareTo(date_now) > 0) {
            response.setStatus(403);
            response.getWriter().write("{\"error\":\"Wrong Date\"}");
            return;
        }
        if (bt.getBlood_sugar() <= 0 && bt.getCholesterol() <= 0 && bt.getIron() <= 0 && bt.getVitamin_b12() <= 0 && bt.getVitamin_d3() <= 0) {
            response.setStatus(406);
            response.getWriter().write("{\"error\":\"You didn't give any measurements\"}");
            return;
        }
        try {
            if (ebtt.databaseToBloodTest(bt.getAmka(), bt.getTest_date()) == null) {
                try {
                    ebtt.addBloodTestFromJSON(test);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(addBloodTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.setStatus(200);
                response.getWriter().write("{\"success\":\"BloodTest Added\"}");
                return;
            } else {
                response.setStatus(403);
                response.getWriter().write("{\"error\":\"BloodTest already exists with the specific AMKA and Test Date.\"}");
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(addBloodTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(addBloodTest.class.getName()).log(Level.SEVERE, null, ex);
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
