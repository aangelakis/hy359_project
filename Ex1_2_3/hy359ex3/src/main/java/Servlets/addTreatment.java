/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.tables.EditBloodTestTable;
import database.tables.EditSimpleUserTable;
import database.tables.EditTreatmentTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mainClasses.BloodTest;
import mainClasses.JSON_Converter;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "addTreatment", urlPatterns = {"/addTreatment"})
public class addTreatment extends HttpServlet {

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
            out.println("<title>Servlet addTreatment</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addTreatment at " + request.getContextPath() + "</h1>");
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
        //  processRequest(request, response);
        Gson gson = new Gson();

        LocalDate date_now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        JSON_Converter jc = new JSON_Converter();

        String data = jc.getJSONFromAjax(request.getReader());

        JsonObject obj = gson.fromJson(data, JsonObject.class);

        LocalDate start_date = LocalDate.parse(obj.get("start_date").getAsString());
        LocalDate end_date = LocalDate.parse(obj.get("end_date").getAsString());

        if (end_date.isBefore(start_date)) {
            response.setStatus(403);
            response.getWriter().write("End Date cannot be before Start Date");
            return;
        }

        EditTreatmentTable ett = new EditTreatmentTable();
        EditSimpleUserTable eut = new EditSimpleUserTable();

        try {
            if (eut.usernameInDataBase(obj.get("username").getAsString())) {
                JsonObject user_id = gson.fromJson(eut.usernameToID(obj.get("username").getAsString()), JsonObject.class);
                HttpSession session = request.getSession();

                JsonObject doctor = gson.fromJson(session.getAttribute("loggedIn").toString(), JsonObject.class);

                EditBloodTestTable ebt = new EditBloodTestTable();

                String user = eut.databaseToJSONID(user_id.get("user_id").getAsInt());
                JsonObject userObj = gson.fromJson(user, JsonObject.class);

                ArrayList<BloodTest> bloodtests = new ArrayList<BloodTest>();
                bloodtests = ebt.databaseToBloodTestAMKA(userObj.get("amka").getAsString());

                if (bloodtests.isEmpty()) {
                    response.setStatus(403);
                    response.getWriter().write("This user has no bloodtests");
                    return;
                }

                int bloodtest_id = bloodtests.get(bloodtests.size() - 1).getBloodtest_id();

                obj.add("doctor_id", doctor.get("doctor_id"));
                obj.add("user_id", user_id.get("user_id"));
                obj.addProperty("bloodtest_id", bloodtest_id);
                obj.remove("username");
                String json = gson.toJson(obj);

                ett.addTreatmentFromJSON(json);
                response.setStatus(200);
            } else {
                response.setStatus(403);
                response.getWriter().write("Username could not be found");
                return;
            }

        } catch (SQLException ex) {
            Logger.getLogger(addRandevouz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(addRandevouz.class.getName()).log(Level.SEVERE, null, ex);
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
