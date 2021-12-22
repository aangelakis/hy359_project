/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.tables.EditDoctorTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.*;

/**
 *
 * @author alex
 */
public class Register_Doctor extends HttpServlet {

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
        JSON_Converter jc = new JSON_Converter();
        Doctor p = jc.jsonToDoctor(request.getReader());

        String JsonString = jc.DoctorToJSON(p);
        String JSON = jc.getJSONFromAjax(request.getReader());
        System.out.println(JSON);
        System.out.println(JsonString);
        EditDoctorTable doctor = new EditDoctorTable();

        /* These 4 lines are for exercise 3, put in comments the try-catch block in order for this to work. */
        //JsonObject xss_json = new JsonParser().parse(JsonString).getAsJsonObject();
        //JsonElement xss = xss_json.get("doctor_info");
        //String xss_out = filter(xss.toString());
        //response.getWriter().write(xss_out);

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            if (doctor.databaseToDoctor(p.getUsername(), p.getPassword()) != null) {
                response.setStatus(409);
                Gson gson = new Gson();
                JsonObject jo = new JsonObject();
                jo.addProperty("error", "Username Already Taken");
                response.getWriter().write(jo.toString());
            } else {
                doctor.addDoctorFromJSON(JsonString);
                response.setStatus(200);
                response.getWriter().write(JsonString);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Register_Doctor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Register_Doctor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String filter(String input) {
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt");
                    break;
                case '>':
                    filtered.append("&gt");
                    break;
                case '&':
                    filtered.append("&amp");
                    break;
                case '"':
                    filtered.append("&quot");
                    break;
                default:
                    filtered.append(c);
            }
        }
        return filtered.toString();
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
