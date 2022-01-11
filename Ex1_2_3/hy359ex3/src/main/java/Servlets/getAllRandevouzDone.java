/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import database.tables.EditSimpleUserTable;
import java.io.IOException;
import java.io.PrintWriter;
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
import mainClasses.Randevouz;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "getAllRandevouzDone", urlPatterns = {"/getAllRandevouzDone"})
public class getAllRandevouzDone extends HttpServlet {

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
            out.println("<title>Servlet getAllRandevouzDone</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getAllRandevouzDone at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        EditRandevouzTable ert = new EditRandevouzTable();
        EditDoctorTable edt = new EditDoctorTable();
        EditSimpleUserTable est = new EditSimpleUserTable();
        ArrayList<Randevouz> ra = null;
        HttpSession session = request.getSession();
        String JSON_user = (String) session.getAttribute("loggedIn");

        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(JSON_user, JsonObject.class);

        try {
            ra = ert.databaseToDoctorRandevouzDone(obj.get("doctor_id").getAsInt(), "done");
            JsonArray json = gson.toJsonTree(ra).getAsJsonArray();

            int i = 0;
            for (i = 0; i < json.size(); i++) {
                JsonObject obj2 = json.get(i).getAsJsonObject();

                String user = est.databaseToJSONID(obj2.get("user_id").getAsInt());
                String doctor = edt.databaseToJSONID(obj2.get("doctor_id").getAsInt());

                JsonObject userObj = gson.fromJson(user, JsonObject.class);
                JsonObject docObj = gson.fromJson(doctor, JsonObject.class);

                String data = obj2.toString();
                String replace = data.replace("user_id", "user_username");
                String replace1 = replace.replace("doctor_id", "doctor_username");

                JsonObject obj3 = gson.fromJson(replace1, JsonObject.class);
                json.set(i, obj3);

                obj3.addProperty("doctor_username", docObj.get("username").getAsString());
                obj3.addProperty("user_username", userObj.get("username").getAsString());

            }
            response.getWriter().write(json.toString());

        } catch (SQLException ex) {
            Logger.getLogger(getAllRandevouzDone.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(getAllRandevouzDone.class.getName()).log(Level.SEVERE, null, ex);
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
