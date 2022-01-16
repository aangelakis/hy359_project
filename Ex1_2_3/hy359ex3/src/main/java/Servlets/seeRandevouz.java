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
import mainClasses.JSON_Converter;
import mainClasses.Randevouz;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "seeRandevouz", urlPatterns = {"/seeRandevouz"})
public class seeRandevouz extends HttpServlet {

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
            out.println("<title>Servlet seeRandevouz</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet seeRandevouz at " + request.getContextPath() + "</h1>");
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
        EditRandevouzTable ert = new EditRandevouzTable();
        EditDoctorTable edt = new EditDoctorTable();
        EditSimpleUserTable est = new EditSimpleUserTable();
        ArrayList<Randevouz> ra = null;
        HttpSession session = request.getSession();
        String JSON_user = (String) session.getAttribute("loggedIn");

        Gson gson = new Gson();

        JSON_Converter jc = new JSON_Converter();

        String date = jc.getJSONFromAjax(request.getReader());

        JsonObject objDate = gson.fromJson(date, JsonObject.class);

        JsonObject obj = gson.fromJson(JSON_user, JsonObject.class);
        try {
            ra = ert.databaseToDoctorRandevouzNotCancelled(obj.get("doctor_id").getAsInt(), "cancelled");

            int j;
            for (j = 0; j < ra.size(); j++) {
                String[] DateTimeSplit = ra.get(j).getDate_time().split(" ");
                String DateSplit = DateTimeSplit[0];
                if (!(objDate.get("date").getAsString().equals(DateSplit))) {
                    ra.remove(j);
                    j = -1;
                }
            }

            JsonArray json = gson.toJsonTree(ra).getAsJsonArray();

            int i;
            for (i = 0; i < json.size(); i++) {
                JsonObject obj2 = json.get(i).getAsJsonObject();

                String doctor = edt.databaseToJSONID(obj2.get("doctor_id").getAsInt());
                JsonObject docObj = gson.fromJson(doctor, JsonObject.class);

                String data = obj2.toString();
                String replace = data.replace("user_id", "user_username");
                String replace1 = replace.replace("doctor_id", "doctor_username");

                JsonObject obj3 = gson.fromJson(replace1, JsonObject.class);
                json.set(i, obj3);

                if (obj2.get("user_id").getAsInt() != 0) {
                    String user = est.databaseToJSONID(obj2.get("user_id").getAsInt());
                    JsonObject userObj = gson.fromJson(user, JsonObject.class);
                    obj3.addProperty("user_username", userObj.get("username").getAsString());
                } else {
                    obj3.addProperty("user_username", "-");
                }

                obj3.addProperty("doctor_username", docObj.get("username").getAsString());
            }

            response.getWriter().write(json.toString());

        } catch (SQLException ex) {
            Logger.getLogger(getAllRandevouz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(getAllRandevouz.class.getName()).log(Level.SEVERE, null, ex);
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
