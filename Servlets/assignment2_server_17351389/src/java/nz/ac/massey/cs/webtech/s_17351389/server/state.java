/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17351389.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sophie Coleman
 */
@WebServlet(name = "state", urlPatterns = {"/state"})
public class state extends HttpServlet {

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
        String format = request.getParameter("format");
        HttpSession session = request.getSession();
        String currentGame = (String)session.getAttribute("currentGame");
        if (currentGame == null){
            ServletContext sc = getServletContext();
            currentGame = (String)sc.getAttribute("currentGame");

            if (currentGame==null){
                currentGame = "___,___,___";
                session.setAttribute("currentGame", "___,___,___");
                sc.setAttribute("currentGame", "___,___,___");
            }
        }

        if (format.equals("txt")){
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            String textFormat = currentGame.replace(",", "\n");
            out.println(textFormat);       
            
        } else if (format.equals("png")) {
            String [] text = currentGame.split(",");

            response.setContentType("image/png");
            ServletOutputStream out = response.getOutputStream();

            BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_BYTE_INDEXED);

            Graphics2D graphics = image.createGraphics();

            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, 200, 200);

            graphics.setPaint(Color.BLACK);
            Font font = new Font("TimesNewRoman", Font.BOLD, 30);
            graphics.setFont(font);

            graphics.drawString(text[0], 5, 30);
            graphics.drawString(text[1], 5, 60);
            graphics.drawString(text[2], 5, 90);

            graphics.dispose();

            javax.imageio.ImageIO.write(image, "png", out);
    
            out.close();

        } else { 
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("Not txt or png");
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
