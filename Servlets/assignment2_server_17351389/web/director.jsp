<%-- 
    Document   : Director
    Created on : 23/05/2020, 1:31:28 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TicTacToe Director Page</title>
    </head>
    <body>
        <h1>Direct to TicTacToe</h1>
        <a href="<%out.print(response.encodeURL("http://localhost:8080/ttt/TTT.jsp"));%>">Go to tictactoe</a>
    </body>
</html>
