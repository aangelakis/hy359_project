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
import database.tables.EditMessageTable;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.Doctor;
import mainClasses.JSON_Converter;
import mainClasses.Message;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "getMessagesDoctor", urlPatterns = {"/getMessagesDoctor"})
public class getMessagesDoctor extends HttpServlet {

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
            out.println("<title>Servlet getMessagesDoctor</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getMessagesDoctor at " + request.getContextPath() + "</h1>");
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
        int doctor_id = doc.getDoctor_id();

        String json = jc.getJSONFromAjax(request.getReader());
        System.out.println(json);
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        System.out.println(type);

        EditMessageTable emt = new EditMessageTable();
        ArrayList<Message> inbox_mes = null;
        ArrayList<Message> sent_mes = null;

        if (type.equals("inbox")) {
            try {
                inbox_mes = emt.databaseToInboxMessageDoctor(doctor_id);
            } catch (SQLException ex) {
                Logger.getLogger(sendMessageToDoctor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(sendMessageToDoctor.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                sent_mes = emt.databaseToSentMessageDoctor(doctor_id);
            } catch (SQLException ex) {
                Logger.getLogger(getMessagesUser.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(getMessagesUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Gson gson = new Gson();
        JsonArray jsonMessages = new JsonArray();
        if (type.equals("inbox")) {
            jsonMessages = gson.toJsonTree(inbox_mes).getAsJsonArray();
        } else {
            jsonMessages = gson.toJsonTree(sent_mes).getAsJsonArray();
        }

        System.out.println(jsonMessages);
        response.setStatus(200);
        response.getWriter().write(jsonMessages.toString());
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
