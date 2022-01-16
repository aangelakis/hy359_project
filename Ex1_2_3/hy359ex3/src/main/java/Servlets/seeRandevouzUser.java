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
import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import java.io.BufferedReader;
import java.io.IOException;
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
import mainClasses.Randevouz;
import mainClasses.SimpleUser;

/**
 *
 * @author alex
 */
public class seeRandevouzUser extends HttpServlet {

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
        int user_id = su.getUser_id();

        EditRandevouzTable ert = new EditRandevouzTable();

        ArrayList<Randevouz> randevouz = null;

        try {
            randevouz = ert.databaseToRandevouzUserIDNotCancelled(user_id);
        } catch (SQLException ex) {
            Logger.getLogger(seeRandevouzUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(seeRandevouzUser.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();
        JsonArray jsonRandevouz = gson.toJsonTree(randevouz).getAsJsonArray();
        response.setStatus(200);
        response.getWriter().write(jsonRandevouz.toString());
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

        EditRandevouzTable ert = new EditRandevouzTable();
        EditDoctorTable edt = new EditDoctorTable();
        ArrayList<Randevouz> ra = null;

        JSON_Converter jc = new JSON_Converter();

        String doctor_id_string = jc.getJSONFromAjax(request.getReader());
        System.out.println(request.getReader());
        System.out.println(doctor_id_string);

        JsonObject jsonObject = new JsonParser().parse(doctor_id_string).getAsJsonObject();
        int doctor_id = Integer.parseInt(jsonObject.get("id").getAsString());

        try {
            ra = ert.databaseToDoctorRandevouzFree(doctor_id);
        } catch (SQLException ex) {
            Logger.getLogger(seeRandevouz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(seeRandevouz.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();
        JsonArray jsonRandevouz = gson.toJsonTree(ra).getAsJsonArray();
        response.getWriter().write(jsonRandevouz.toString());

        response.setStatus(200);
        System.out.println("Randevouz\n" + jsonRandevouz + "\n");

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
