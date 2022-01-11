/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditTreatmentTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.JSON_Converter;
import mainClasses.SimpleUser;
import mainClasses.Treatment;

/**
 *
 * @author alex
 */
public class getTreatment extends HttpServlet {

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

        HttpSession session = request.getSession();
        response.setStatus(200);

        String JSON_user = (String) session.getAttribute("loggedIn").toString();
        JSON_Converter jc = new JSON_Converter();
        Reader inputString = new StringReader(JSON_user);
        BufferedReader reader = new BufferedReader(inputString);
        SimpleUser su = jc.jsonToSimpleUser(reader);
        //SimpleUser su = (SimpleUser) u;
        System.out.println(su.getUser_id());

        EditTreatmentTable ett = new EditTreatmentTable();

        ArrayList<Treatment> treatments = null;

        try {
            if ((treatments = ett.databaseToTreatmentUserID(su.getUser_id())) != null) {
                Gson gson = new Gson();
                JsonArray jsonDoc = gson.toJsonTree(treatments).getAsJsonArray();
                response.setStatus(200);
                response.getWriter().write(jsonDoc.toString());
            } else {
                response.setStatus(403);
                response.getWriter().write("{\"error\":\"There is no treatments for this user.\"}");
            }
        } catch (SQLException ex) {
            Logger.getLogger(addBloodTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(addBloodTest.class.getName()).log(Level.SEVERE, null, ex);
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

        JSON_Converter jc = new JSON_Converter();

        String bloodtest_id_string = jc.getJSONFromAjax(request.getReader());

        System.out.println(bloodtest_id_string);

        JsonObject jsonObject = new JsonParser().parse(bloodtest_id_string).getAsJsonObject();
        int bloodtest_id = Integer.parseInt(jsonObject.get("id").getAsString());

        System.out.println(bloodtest_id);

        EditTreatmentTable ett = new EditTreatmentTable();

        ArrayList<Treatment> t = new ArrayList<Treatment>();
        try {
            t = ett.databaseToTreatmentBloodtestID(bloodtest_id);
        } catch (SQLException ex) {
            Logger.getLogger(CertifyDoctors.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CertifyDoctors.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();
        JsonArray jsonTreatments = gson.toJsonTree(t).getAsJsonArray();

        System.out.println(jsonTreatments);
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonTreatments.toString());
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
