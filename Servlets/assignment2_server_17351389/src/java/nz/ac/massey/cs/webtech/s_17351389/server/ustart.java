/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17351389.server;

import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sophie Coleman
 */
@WebServlet(name = "ustart", urlPatterns = {"/ustart"})
public class ustart extends HttpServlet {

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
    private String place (char p, int x, int y, String currentGame){
        switch (y) {
            case 1:
                currentGame = currentGame.substring(0,(x-1)) + p + currentGame.substring(x);
                break;
            case 2:
                currentGame = currentGame.substring(0,(x+3)) + p + currentGame.substring(x+4);
                break;
            case 3:
                currentGame = currentGame.substring(0,(x+7)) + p + currentGame.substring(x+8);
                break;
            default:
                return currentGame;
        }
       return currentGame;
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
        processRequest(request, response);
        response.setContentType("text/plain;charset=UTF-8");
        HttpSession session = request.getSession();
        String currentGame = "___,___,___";
        session.setAttribute("currentGame", "___,___,___");
        ServletContext sc = getServletContext();
        sc.setAttribute("currentGame", "___,___,___");
        sc.setAttribute("winner", "none");

        Random r=new Random();

        int computerx = r.nextInt(3)+1;
        int computery = r.nextInt(3)+1;

        currentGame = place('o', computerx, computery, currentGame);
        session.setAttribute("currentGame", currentGame);
        sc.setAttribute("currentGame", currentGame);
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
