/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.tables.EditBloodTestTable;
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
import mainClasses.BloodTest;
import mainClasses.JSON_Converter;
import mainClasses.Randevouz;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "SeeBloodTestsDoctor", urlPatterns = {"/SeeBloodTestsDoctor"})
public class SeeBloodTestsDoctor extends HttpServlet {

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
            out.println("<title>Servlet SeeBloodTestsDoctor</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SeeBloodTestsDoctor at " + request.getContextPath() + "</h1>");
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
        // processRequest(request, response);
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
        //processRequest(request, response);
        JSON_Converter jc = new JSON_Converter();
        Gson gson = new Gson();
        int count = 0;

        String data = jc.getJSONFromAjax(request.getReader());
        JsonObject dataObj = gson.fromJson(data, JsonObject.class);



        EditSimpleUserTable est = new EditSimpleUserTable();
        EditRandevouzTable ert = new EditRandevouzTable();
        EditBloodTestTable ebt = new EditBloodTestTable();

        try {

            String user = est.databaseUserToJSONUsername(dataObj.get("username").getAsString());

            if (user == null) {
                response.setStatus(403);
                response.getWriter().write("User could not be found");
                return;
            }
            JsonObject userObj = gson.fromJson(user, JsonObject.class);
            ArrayList<Randevouz> randevouz = new ArrayList<Randevouz>();

            randevouz = ert.databaseToRandevouzUserID(userObj.get("user_id").getAsInt());
            for (Randevouz ra : randevouz) {
                if ("done".equals(ra.getStatus())) {
                    count++;
                }
            }

            if (count == 0) {
                response.setStatus(403);
                response.getWriter().write("No Randevouz with 'done' status for this user");
                return;
            } else {
                String amka = userObj.get("amka").getAsString();
                ArrayList<BloodTest> bloodtests = new ArrayList<BloodTest>();
                bloodtests = ebt.databaseToBloodTestAMKA(amka);
                JsonArray json = gson.toJsonTree(bloodtests).getAsJsonArray();
                response.getWriter().write(json.toString());
            }

        } catch (SQLException ex) {
            Logger.getLogger(SeeBloodTestsDoctor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SeeBloodTestsDoctor.class.getName()).log(Level.SEVERE, null, ex);
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
