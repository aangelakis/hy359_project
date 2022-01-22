/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import database.tables.EditDoctorTable;
import database.tables.EditRandevouzTable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.Doctor;
import mainClasses.Randevouz;

/**
 *
 * @author alex
 */
public class getDoctorsByPrice extends HttpServlet {

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
        EditDoctorTable edt = new EditDoctorTable();
        EditRandevouzTable ert = new EditRandevouzTable();
        ArrayList<Doctor> doctors = new ArrayList<Doctor>();
        ArrayList<ArrayList<Randevouz>> randevouz = new ArrayList<>();
        try {
            doctors = edt.databaseToDoctorsCertified();
        } catch (SQLException ex) {
            Logger.getLogger(getDoctorsByPrice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(getDoctorsByPrice.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (int i = 0; i < doctors.size(); i++) {
            try {
                randevouz.add(ert.databaseToDoctorRandevouz(doctors.get(i).getDoctor_id()));
            } catch (SQLException ex) {
                Logger.getLogger(getDoctorsByPrice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(getDoctorsByPrice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int[] doctors_id = new int[doctors.size()];
        int[] prices = new int[doctors.size()];

        for (int i = 0; i < doctors.size(); i++) {
            if (randevouz.get(i).size() == 0) {
                doctors_id[i] = doctors.get(i).getDoctor_id();
                prices[i] = 0;
            } else {
                doctors_id[i] = randevouz.get(i).get(0).getDoctor_id();
                prices[i] = randevouz.get(i).get(0).getPrice();
            }
        }

        // Bubble sort prices
        for (int i = 0; i < doctors.size() - 1; i++) {
            for (int j = 0; j < doctors.size() - i - 1; j++) {
                if (prices[j] > prices[j + 1]) {
                    int temp = prices[j];
                    prices[j] = prices[j + 1];
                    prices[j + 1] = temp;
                    temp = doctors_id[j];
                    doctors_id[j] = doctors_id[j + 1];
                    doctors_id[j + 1] = temp;
                }
            }
        }

        ArrayList<Doctor> sortedDoctors = new ArrayList<>();
        for (int i = 0; i < doctors.size(); i++) {
            try {
                sortedDoctors.add(edt.databaseToDoctorID(doctors_id[i]));
            } catch (SQLException ex) {
                Logger.getLogger(getDoctorsByPrice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(getDoctorsByPrice.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Gson gson = new Gson();
        JsonArray jsonDoc = gson.toJsonTree(sortedDoctors).getAsJsonArray();

        for (int i = 0; i < sortedDoctors.size(); i++) {
            jsonDoc.get(i).getAsJsonObject().addProperty("price", prices[i]);
        }
        response.getWriter().write(jsonDoc.toString());
        response.setStatus(200);
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
