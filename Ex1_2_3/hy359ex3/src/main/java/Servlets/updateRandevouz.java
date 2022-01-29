/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.tables.EditRandevouzTable;
import database.tables.EditSimpleUserTable;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
import mainClasses.JSON_Converter;
import mainClasses.Randevouz;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "updateRandevouz", urlPatterns = {"/updateRandevouz"})
public class updateRandevouz extends HttpServlet {

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
            out.println("<title>Servlet updateRandevouz</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet updateRandevouz at " + request.getContextPath() + "</h1>");
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
        Gson gson = new Gson();

        LocalDate date_now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        JSON_Converter jc = new JSON_Converter();

        String data = jc.getJSONFromAjax(request.getReader());

        JsonObject obj = gson.fromJson(data, JsonObject.class);

        if (obj.has("status")) {
            if (!"free".equals(obj.get("status").getAsString()) && !"selected".equals(obj.get("status").getAsString()) && !"cancelled".equals(obj.get("status").getAsString()) && !"done".equals(obj.get("status").getAsString())) {
                response.setStatus(403);
                response.getWriter().write("This is not a valid status");
                return;
            }
        }

        EditRandevouzTable ert = new EditRandevouzTable();
        EditSimpleUserTable eut = new EditSimpleUserTable();

        try {
            String json = gson.toJson(obj);

            HttpSession session = request.getSession();
            JsonObject doctor = gson.fromJson(session.getAttribute("loggedIn").toString(), JsonObject.class);

            Randevouz rz = gson.fromJson(data, Randevouz.class);
            String DateTime = rz.getDate_time();
            if (rz.getDate_time() != null) {
                String[] DateTimeSplit = DateTime.split(" ");
                String Date = DateTimeSplit[0];
                String Time = DateTimeSplit[1];

                    LocalDate rz_date = LocalDate.parse(Date, formatter);

                    LocalTime early = LocalTime.parse("08:00");
                    LocalTime late = LocalTime.parse("20:30");
                    LocalTime mytime = LocalTime.parse(Time);

                    if (mytime.isBefore(early) || mytime.isAfter(late)) {
                        response.setStatus(403);
                        response.getWriter().write("Enter a time from 08:00 to 20:30");
                        return;
                    }

                if (rz_date.compareTo(date_now) <= 0) {
                    response.setStatus(403);
                    response.getWriter().write("Wrong Date");
                    return;
                }


                ArrayList<Randevouz> ra = ert.databaseToDoctorRandevouzNotCancelled(doctor.get("doctor_id").getAsInt(), "cancelled");
                for (Randevouz a : ra) {
                    if (a.getRandevouz_id() != obj.get("randevouz_id").getAsInt()) {
                        String[] DateTimeSplit2 = a.getDate_time().split(" ");
                        String Date2 = DateTimeSplit2[0];
                        String Time2 = DateTimeSplit2[1];
                        LocalTime mytime2 = LocalTime.parse(Time2);

                        if (Date2.equals(Date)) {
                            Duration d1 = Duration.between(mytime, mytime2);
                            if ((d1.getSeconds() / 60) > 0 && (d1.getSeconds() / 60) < 30) {
                                response.setStatus(403);
                                response.getWriter().write("There is another randevouz less than 30 minutes later than this one");
                                return;
                            } else if ((d1.getSeconds() / 60) < 0 && (d1.getSeconds() / 60) > -30) {
                                response.setStatus(403);
                                response.getWriter().write("There is another randevouz less than 30 minutes earlier than this one");
                                return;
                            } else if ((d1.getSeconds() / 60) == 0) {
                                response.setStatus(403);
                                response.getWriter().write("There is another randevouz exactly at the time you entered");
                                return;
                            }

                        }
                    }
                }
            }
            Randevouz ran = ert.databaseToRandevouz(obj.get("randevouz_id").getAsInt());
            String date_time;
            String doc_info;
            String status;
            int price;

            if (!obj.has("date_time")) {
                date_time = ran.getDate_time();
            } else {
                date_time = obj.get("date_time").getAsString();
            }

            if (!obj.has("doctor_info")) {
                doc_info = ran.getDoctor_info();
            } else {
                doc_info = obj.get("doctor_info").getAsString();
            }

            if (!obj.has("price")) {
                price = ran.getPrice();
            } else {
                price = obj.get("price").getAsInt();
            }

            if (!obj.has("status")) {
                status = ran.getStatus();
            } else {
                status = obj.get("status").getAsString();
            }

            if (!ran.getStatus().equals("done")) {
                ert.updateRandevouzDoctor(obj.get("randevouz_id").getAsInt(), date_time, price, doc_info, status);
                response.setStatus(200);
            } else {
                response.getWriter().write("You cannot change a done randevouz");
                response.setStatus(403);
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
