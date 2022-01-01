/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import database.tables.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 *
 * @author alex
 */
public class Login extends HttpServlet {

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
        String JSON_user = (String) session.getAttribute("loggedIn");

        if (JSON_user != null) {
            response.setStatus(200);
            response.getWriter().write(JSON_user);

        } else {
            response.setStatus(403);
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        int flag = 0;
        String JSON_user;
        EditSimpleUserTable eut = new EditSimpleUserTable();
        EditDoctorTable edt = new EditDoctorTable();
        HttpSession session = request.getSession(true);
        try {
            if ((JSON_user = eut.databaseUserToJSON(username, password)) != null) {
                /* If the user is the admin */
                if (username.equals("admin") && password.equals("admin12*")) {
                    response.getWriter().write("admin");
                } else {
                    response.getWriter().write("user");
                }
                session.setAttribute("loggedIn", JSON_user);
                response.setStatus(200);
                flag = 1;

            } else {
                response.setStatus(403);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (flag == 0) {
            try {
                if ((JSON_user = edt.databaseToJSON_Certified(username, password)) != null) {
                    response.getWriter().write("doc");
                    session.setAttribute("loggedIn", JSON_user);
                    response.setStatus(200);
                } else if ((JSON_user = edt.databaseToJSON(username, password)) != null) {
                    response.getWriter().write("not certified");
                    response.setStatus(403);
                } else {
                    response.getWriter().write("wrong credentials");
                    response.setStatus(403);
                }
            } catch (SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
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
