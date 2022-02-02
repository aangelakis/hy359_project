/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mainClasses.JSON_Converter;

/**
 *
 * @author ΜΙΧΑΛΗΣ
 */
@WebServlet(name = "PDFServlet", urlPatterns = {"/PDFServlet"})
public class PDFServlet extends HttpServlet {

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet PDFServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PDFServlet at " + request.getContextPath() + "</h1>");
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
        // processRequest(request, response);

        JSON_Converter jc = new JSON_Converter();
        String json = jc.getJSONFromAjax(request.getReader());

        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(json);
        JsonArray jsonArr = tradeElement.getAsJsonArray();

        System.out.println(jsonArr);

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("d:/Randevouz.pdf"));

        } catch (DocumentException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        document.open();

        PdfPTable table = new PdfPTable(8);
        float[] widths = new float[]{50f, 65f, 60f, 47f, 20f, 50f, 50f, 35f};
        try {
            table.setWidths(widths);
        } catch (DocumentException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        table.setWidthPercentage(110);

        addTableHeader(table);
        addCustomRows(table, jsonArr);

        try {
            document.add(table);
        } catch (DocumentException ex) {
            Logger.getLogger(PDFServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        document.close();
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

    private void addTableHeader(PdfPTable table) {
        Stream.of("randevouz_id", "doctor_username", "user_username", "date_time", "price", "doctor_info", "user_info", "status")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addCustomRows(PdfPTable table, JsonArray ja) {
        for (int i = 0; i < ja.size(); i++) {
            JsonObject propertiesJson = ja.get(i).getAsJsonObject();
            String[] properties = new String[8];
            properties[0] = propertiesJson.get("randevouz_id").getAsString();
            properties[1] = propertiesJson.get("doctor_username").getAsString();
            properties[2] = propertiesJson.get("user_username").getAsString();
            properties[3] = propertiesJson.get("date_time").getAsString();
            properties[4] = propertiesJson.get("price").getAsString();
            properties[5] = propertiesJson.get("doctor_info").getAsString();
            properties[6] = propertiesJson.get("user_info").getAsString();
            properties[7] = propertiesJson.get("status").getAsString();
            for (int j = 0; j < 8; j++) {
                table.addCell(properties[j]);
            }
        }
    }
}
