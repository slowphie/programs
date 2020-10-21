/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.s_17351389.server;

import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "move", urlPatterns = {"/move/*"})
public class move extends HttpServlet {

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
    
    private boolean checkValid (char p, int x, int y, String currentGame){
        if (y==1 && currentGame.charAt(x-1)==p){
            return false;
       } else if (y==2 && currentGame.charAt(x+3)==p){
            return false;            
       } else if (y==3 && currentGame.charAt(x+7)==p){
            return false;
       } else {
           return true;
       }
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
    
    private String getPossibleMove(String spaces){
        String[] moves = spaces.split("\n");
        Random r=new Random();
        int randomNumber=r.nextInt(moves.length);
        
        return moves[randomNumber];

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int x = Integer.parseInt(request.getPathInfo().substring(2,3));
            int y = Integer.parseInt(request.getPathInfo().substring(4));
            ServletContext sc = getServletContext();
            HttpSession session = request.getSession();
            String cookie = request.getHeader("cookie");
            String urlSession = request.getRequestURI();

            if ((session.isNew()) && cookie==null && (!urlSession.contains("jsessionid"))){
                response.setStatus(404, "Not Found"); 
            } else {
                String currentGame = (String)session.getAttribute("currentGame");
                if (currentGame==null){
                    currentGame = (String)sc.getAttribute("currentGame");
                }

                if (currentGame==null){
                    response.setStatus(404, "Not Found");

                } else{

                    if (checkValid('x', x, y, currentGame)){
                        currentGame = place('x', x, y, currentGame);
                        session.setAttribute("currentGame", currentGame);
                        sc.setAttribute("currentGame", currentGame);
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
                        String winner = (String)sc.getAttribute("winner");
                        if (winner.contains("none")){
                            String move = getPossibleMove(getSpaces(currentGame));
                            int computerx = Integer.parseInt(move.split(",")[0]);
                            int computery = Integer.parseInt(move.split(",")[1]);

                            currentGame = place('o', computerx, computery, currentGame);
                            session.setAttribute("currentGame", currentGame);
                            sc.setAttribute("currentGame", currentGame);

                        }
                        
                    } else {
                        response.setStatus(400, "Bad Request");
                    } 
                }

            }
        } catch(Exception e){
            response.setStatus(400, "Bad Request");
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
