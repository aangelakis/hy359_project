/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import database.tables.EditDoctorTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.SQLException;
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
import mainClasses.User;

/**
 *
 * @author alex
 */
public class getDoctorsSortedDistance extends HttpServlet {

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
        //response.setStatus(200);

        String JSON_user = (String) session.getAttribute("loggedIn").toString();
        JSON_Converter jc = new JSON_Converter();
        Reader inputString = new StringReader(JSON_user);
        BufferedReader reader = new BufferedReader(inputString);
        User su = jc.jsonToUser(reader);
        Double lat = su.getLat();
        Double lon = su.getLon();

        EditDoctorTable edt = new EditDoctorTable();
        ArrayList<Doctor> doctors = null;
        //ArrayList<Doctor> doctorsSorted = new ArrayList<>();
        try {
            doctors = edt.databaseToDoctorsCertified();
        } catch (SQLException ex) {
            Logger.getLogger(getDoctors.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(getDoctors.class.getName()).log(Level.SEVERE, null, ex);
        }

        Gson gson = new Gson();
        JsonArray jsonDoc = gson.toJsonTree(doctors).getAsJsonArray();
        //JsonArray jsonDocSorted = new JsonArray();
        double distance[] = new double[doctors.size()];

        for (int i = 0; i < jsonDoc.size(); i++) {
            double doc_lat = jsonDoc.get(i).getAsJsonObject().get("lat").getAsDouble();
            double doc_lon = jsonDoc.get(i).getAsJsonObject().get("lon").getAsDouble();

            distance[i] = findDistance(lat, doc_lat, lon, doc_lon);
        }

        int n = distance.length;
        for (int i = 0; i < n; i++) {
            System.out.println(distance[i]);
        }

        System.out.println("After");

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (distance[j] > distance[j + 1]) {
                    double temp = distance[j];
                    Doctor temp1 = doctors.get(j);
                    distance[j] = distance[j + 1];
                    distance[j + 1] = temp;
                    doctors.set(j, doctors.get(j + 1));
                    doctors.set(j + 1, temp1);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println(distance[i]);
        }

        jsonDoc = gson.toJsonTree(doctors).getAsJsonArray();
        response.getWriter().write(jsonDoc.toString());

        response.setStatus(200);
        System.out.println("Doctors\n" + jsonDoc + "\n");

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

    public double findDistance(double lat1, double lat2, double lon1, double lon2) {
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
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
