/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditMessageTable;
import database.tables.EditSimpleUserTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.Doctor;
import mainClasses.JSON_Converter;
import mainClasses.SimpleUser;

/**
 *
 * @author alex
 */
public class sendEmergencyMessage extends HttpServlet {

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
        HttpSession session = request.getSession();
        String JSON_user = (String) session.getAttribute("loggedIn").toString();
        JSON_Converter jc = new JSON_Converter();
        Reader inputString = new StringReader(JSON_user);
        BufferedReader reader = new BufferedReader(inputString);
        Doctor doc = jc.jsonToDoctor(reader);

        String json = jc.getJSONFromAjax(request.getReader());
        System.out.println(json);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();

        String bloodtype = jsonObject.get("bloodtype").getAsString();
        String message = jsonObject.get("message").getAsString();

        EditSimpleUserTable esut = new EditSimpleUserTable();
        ArrayList<SimpleUser> users = new ArrayList<SimpleUser>();

        try {
            users = esut.databaseToSimpleUsersBloodtypes(bloodtype);
        } catch (SQLException ex) {
            Logger.getLogger(sendEmergencyMessage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(sendEmergencyMessage.class.getName()).log(Level.SEVERE, null, ex);
        }

        EditMessageTable emt = new EditMessageTable();
        int doctor_id = doc.getDoctor_id();
        int user_id;
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().withNano(0).toString();
        String date_time = date + " " + time;

        int ids[] = new int[users.size()];

        for (int i = 0; i < users.size(); i++) {

            JsonObject json_message = new JsonObject();
            user_id = users.get(i).getUser_id();
            ids[i] = user_id;
            json_message.addProperty("doctor_id", doctor_id);
            json_message.addProperty("message", message);
            json_message.addProperty("user_id", user_id);
            json_message.addProperty("sender", "doctor");
            json_message.addProperty("date_time", date_time);
            json_message.addProperty("blood_donation", 1);
            json_message.addProperty("bloodtype", bloodtype);

            String json_message_string = json_message.toString();

            try {
                emt.addMessageFromJSON(json_message_string);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(sendMessageToDoctor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        response.getWriter().write(Arrays.toString(ids));
        response.setStatus(200);
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
