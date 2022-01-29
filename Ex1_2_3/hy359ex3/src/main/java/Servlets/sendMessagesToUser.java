/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.tables.EditMessageTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.Doctor;
import mainClasses.JSON_Converter;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "sendMessagesToUser", urlPatterns = {"/sendMessagesToUser"})
public class sendMessagesToUser extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet sendMessagesToUser</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet sendMessagesToUser at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        processRequest(request, response);
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
        // processRequest(request, response);
        HttpSession session = request.getSession();
        String JSON_doctor = (String) session.getAttribute("loggedIn").toString();
        JSON_Converter jc = new JSON_Converter();
        Reader inputString = new StringReader(JSON_doctor);
        BufferedReader reader = new BufferedReader(inputString);
        Doctor doc = jc.jsonToDoctor(reader);

        String json = jc.getJSONFromAjax(request.getReader());
        System.out.println(json);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().withNano(0).toString();

        int doctor_id = doc.getDoctor_id();
        String message = jsonObject.get("message").getAsString();
        int user_id = Integer.parseInt(jsonObject.get("id").getAsString());
        String date_time = date + " " + time;
        String bloodtype = doc.getBloodtype();
        int blood_donation = doc.getBlooddonor();

        JsonObject json_message = new JsonObject();
        json_message.addProperty("doctor_id", doctor_id);
        json_message.addProperty("message", message);
        json_message.addProperty("user_id", user_id);
        json_message.addProperty("sender", "user");
        json_message.addProperty("date_time", date_time);
        json_message.addProperty("blood_donation", blood_donation);
        json_message.addProperty("bloodtype", bloodtype);

        EditMessageTable emt = new EditMessageTable();
        String json_message_string = json_message.toString();
        try {
            emt.addMessageFromJSON(json_message_string);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(sendMessageToDoctor.class.getName()).log(Level.SEVERE, null, ex);
        }

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
