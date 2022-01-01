/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditDoctorTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.JSON_Converter;

/**
 *
 * @author alex
 */
public class CertifyDoctors extends HttpServlet {

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

        JSON_Converter jc = new JSON_Converter();

        String doctor_id_string = jc.getJSONFromAjax(request.getReader());

        System.out.println(doctor_id_string);

        JsonObject jsonObject = new JsonParser().parse(doctor_id_string).getAsJsonObject();
        int doctor_id = Integer.parseInt(jsonObject.get("id").getAsString());

        System.out.println(doctor_id);

        EditDoctorTable doctor = new EditDoctorTable();

        try {
            doctor.certifyDoctor(doctor_id);
        } catch (SQLException ex) {
            Logger.getLogger(CertifyDoctors.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CertifyDoctors.class.getName()).log(Level.SEVERE, null, ex);
        }

        String doc = null;
        try {
            doc = doctor.databaseToJSONID(doctor_id);
        } catch (SQLException ex) {
            Logger.getLogger(CertifyDoctors.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CertifyDoctors.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.setStatus(200);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(doc);
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
