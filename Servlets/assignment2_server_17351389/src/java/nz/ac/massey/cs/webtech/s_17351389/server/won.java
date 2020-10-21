/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17351389.server;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "won", urlPatterns = {"/won"})
public class won extends HttpServlet {

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
    
    private boolean checkWon(char p, String ppp, String currentGame){

        if (currentGame.contains(ppp)){
            return true;            
        } else {
            String[] splitGame = currentGame.split(",");
            for (int i = 0; i<3; i++){
                if (splitGame[0].charAt(i) == p && splitGame[1].charAt(i) == p && splitGame[2].charAt(i) == p){
                    return true;
                }
            } 
            if (splitGame[0].charAt(0) == p && splitGame[1].charAt(1) == p && splitGame[2].charAt(2) == p){
                return true;
            } else if (splitGame[0].charAt(2) == p && splitGame[1].charAt(1) == p && splitGame[2].charAt(0) == p){
                return true;
            }
        }
        return false;
        
    }
    
    private String getSpaces(String currentGame){
        String spaces = "";
        String[] splitGame = currentGame.split(",");
        for (int y = 0; y<3; y++){
            for(int x =0; x<3;x++){
                if (splitGame[y].charAt(x)=='_'){
                    spaces = spaces+(x+1)+","+(y+1)+"\n";
                }
            }
        }
        return spaces;
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
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
            /* TODO output your page here. You may use following sample code. */
        try {
            String includeRequest = (String) request.getAttribute("javax.servlet.include.request_uri");
            String cookie = request.getHeader("cookie");
            String urlSession = request.getRequestURI();
            HttpSession session = request.getSession();

            if ((session.isNew()) && includeRequest==null && cookie==null && (!urlSession.contains("jsessionid"))){
                System.out.println("Not found session");

                response.setStatus(404, "Not Found"); 

            } else {
                ServletContext sc = getServletContext();
                String currentGame = (String)session.getAttribute("currentGame");
                if (currentGame == null){
                    currentGame = (String)sc.getAttribute("currentGame");
                }
                if (currentGame != null){
                    boolean xWon = checkWon('x', "xxx", currentGame);
                    boolean oWon = checkWon('o', "ooo", currentGame);
                    String possiblemoves = getSpaces(currentGame);
                    boolean draw = false;
                    if (possiblemoves.equals("")){
                        draw=true;
                    }
                    if (xWon){
                        out.print("user");
                        sc.setAttribute("winner", "user");  
                        session.invalidate();
                    } else if (oWon){
                        out.print("computer");
                        sc.setAttribute("winner", "computer");
                        session.invalidate();
                    } else if (draw) {
                        out.print("draw");
                        sc.setAttribute("winner", "draw");
                        session.invalidate();
                    } else {
                        sc.setAttribute("winner", "none");
                        out.print("none");
                    }
                } else {
                    System.out.println("Not found game");
                    response.setStatus(404, "Not Found"); 
                }
            }

        } catch (Exception e){
            System.out.println("Error has occured"+e);
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
