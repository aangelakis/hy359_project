/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.JsonObject;
import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.Doctor;
import mainClasses.JSON_Converter;
import mainClasses.Randevouz;
import mainClasses.SimpleUser;

/**
 *
 * @author alex
 */
public class alertUser extends HttpServlet {

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
        String JSON_user = (String) session.getAttribute("loggedIn").toString();
        JSON_Converter jc = new JSON_Converter();
        Reader inputString = new StringReader(JSON_user);
        BufferedReader reader = new BufferedReader(inputString);
        SimpleUser su = jc.jsonToSimpleUser(reader);
        int user_id = su.getUser_id();

        EditRandevouzTable ert = new EditRandevouzTable();
        ArrayList<Randevouz> randevouz = new ArrayList<Randevouz>();

        try {
            randevouz = ert.databaseToRandevouzUserID(user_id);
        } catch (SQLException ex) {
            Logger.getLogger(alertUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(alertUser.class.getName()).log(Level.SEVERE, null, ex);
        }

        LocalDate date_now = java.time.LocalDate.now();
        LocalTime time_now = java.time.LocalTime.now().withNano(0);
        boolean alert = false;
        int i = 0;
        String dateString, timeString;
        LocalDate date;
        LocalTime time = null;
        if (randevouz != null) {

            for (i = 0; i < randevouz.size(); i++) {
                String date_time = randevouz.get(i).getDate_time();
                dateString = date_time.substring(0, 10);
                date = LocalDate.parse(dateString);
                timeString = date_time.substring(11, 19);
                time = LocalTime.parse(timeString);

                if (time_now.until(time, ChronoUnit.HOURS) <= 4 && date_now.equals(date)) {
                    System.out.println(time_now + " " + time);
                    alert = true;
                    break;
                }
            }
        }

        if (alert) {
            EditDoctorTable edt = new EditDoctorTable();
            Doctor doc = null;
            long diff = time_now.until(time, ChronoUnit.HOURS);
            /*int blooddonor = 0;
                String sender = "system";
                int doctor_id = 0;
                String bloodtype = null;
                String message = "Your randevouz is in " + difference + " hours";
                String date = java.time.LocalDate.now().toString();
                String time = java.time.LocalTime.now().toString();
                String date_time = date + " " + time;
             */
            try {
                doc = edt.databaseToDoctorID(randevouz.get(i).getDoctor_id());

            } catch (SQLException ex) {
                Logger.getLogger(alertUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(alertUser.class.getName()).log(Level.SEVERE, null, ex);
            }

            JsonObject json_message = new JsonObject();
            json_message.addProperty("lastname", doc.getLastname());
            json_message.addProperty("diff", diff);
            /*json_message.addProperty("doctor_id", doctor_id);
            json_message.addProperty("message", message);
            json_message.addProperty("user_id", user_id);
            json_message.addProperty("sender", "user");
            json_message.addProperty("date_time", date_time);
            json_message.addProperty("blood_donation", blooddonor);
            json_message.addProperty("bloodtype", bloodtype);
            
             */
            String json_message_string = json_message.toString();
            response.getWriter().write(json_message_string);
            /*EditMessageTable emt = new EditMessageTable();

            try {
                emt.addMessageFromJSON(json_message_string);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(sendMessageToDoctor.class.getName()).log(Level.SEVERE, null, ex);
            }
             */
            response.setStatus(200);

        } else {
            response.setStatus(403);
        }
        return;
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
        processRequest(request, response);
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
