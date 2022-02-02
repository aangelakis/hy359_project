/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import database.tables.EditMessageTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.JSON_Converter;
import mainClasses.Message;
import mainClasses.SimpleUser;

/**
 *
 * @author alex
 */
public class alertUserEmergency extends HttpServlet {

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

        EditMessageTable emt = new EditMessageTable();
        ArrayList<Message> messages = new ArrayList<Message>();

        try {
            messages = emt.databaseToInboxMessageUser(user_id);
        } catch (SQLException ex) {
            Logger.getLogger(alertUserEmergency.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(alertUserEmergency.class.getName()).log(Level.SEVERE, null, ex);
        }

        LocalTime time_now = java.time.LocalTime.now().withNano(0);
        String time_string;
        LocalTime message_time;
        LocalTime two_minutes_before = time_now.minusMinutes(2);
        for (int i = 0; i < messages.size(); i++) {
            time_string = messages.get(i).getDate_time().substring(11, 19);
            System.out.println(time_string);
            message_time = LocalTime.parse(time_string);
            if (message_time.isAfter(two_minutes_before) && message_time.isBefore(time_now)) {
                response.setStatus(200);
                return;
            }
        }

        response.setStatus(403);
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
