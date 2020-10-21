<%-- 
    Document   : TTT
    Created on : 21/05/2020, 11:21:38 PM
    Author     : Sophie Coleman
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*,javax.servlet.http.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>TicTacToe</title>
        <style>
            table {
                border-collapse: collapse;
                display: none;
                
            }

            table, th, td {
                border: 1px solid black;
                padding: 30px 20px;
                font-size: 30px;
        
            }

            td:hover {
                background-color: lightsteelblue;
            }

            a{
                text-decoration: none;
                padding: 40px 20px;
            }
            
            #winner {
                color: green;
            }
            #error {
                color: red;
            }
        </style>
        <script type=text/javascript"></script>
        <script>
            window.onload = function() {
            updateBoard();
            var xmlHttpWon1 = new XMLHttpRequest();
                xmlHttpWon1.open( "GET", "<%out.print(response.encodeURL("http://localhost:8080/ttt/won"));%>", false);
                xmlHttpWon1.send();
                if(!xmlHttpWon1.responseText.includes('none')){                        
                    if (!xmlHttpWon1.responseText.includes('draw')){
                        document.getElementById("winner").innerHTML = "Winner: " + xmlHttpWon1.responseText;
                    } else {
                        document.getElementById("winner").innerHTML = "No winner game is a " + xmlHttpWon1.responseText;

                    }

                }

                document.getElementById("gameboard").style.display = "block";
                
            }
            function updateBoard(){
                    
                    var xmlHttp = new XMLHttpRequest();
                    xmlHttp.open( "GET", "<%out.print(response.encodeURL("http://localhost:8080/ttt/state?format=txt"));%>", false);
                    xmlHttp.send();
                    
                    var board= xmlHttp.responseText;
                    board = board.substring(0,3)+board.substring(4,7)+board.substring(8);
                    
                    for (var i = 1; i<10; i++){
                        document.getElementById(i.toString()).innerHTML = board.charAt(i-1);
                    }
                    
  
                }
                
            function move(url, pos){
                document.getElementById("error").innerHTML = "";
                var won = document.getElementById("winner").innerHTML;
                if(!won){ 
                    var xmlHttp = new XMLHttpRequest();
                    xmlHttp.open( "GET", "<%out.print(response.encodeURL("http://localhost:8080/ttt/possiblemoves"));%>", false);
                    xmlHttp.send();
                    
                    if(xmlHttp.responseText.includes(pos)){                
                        var xmlHttpMove = new XMLHttpRequest();
                        xmlHttpMove.open( "POST", url, false);
                        xmlHttpMove.send();
                        
                        if(xmlHttpMove.status == 200){

                            var xmlHttpWon1 = new XMLHttpRequest();
                            xmlHttpWon1.open( "GET", "<%out.print(response.encodeURL("http://localhost:8080/ttt/won"));%>", false);
                            xmlHttpWon1.send();
                            if(!xmlHttpWon1.responseText.includes('none')){                        
                                if (!xmlHttpWon1.responseText.includes('draw')){
                                    document.getElementById("winner").innerHTML = "Winner: " + xmlHttpWon1.responseText;
                                } else {
                                    document.getElementById("winner").innerHTML = "No winner game is a " + xmlHttpWon1.responseText;

                                }

                            }

                            updateBoard();
                        }

                    } else {
                        console.log(xmlHttp.responseText);
                        document.getElementById("error").innerHTML = "Invalid move";
                    }
                } else {
                    document.getElementById("error").innerHTML = "Game is finished.";
                }
            }
            
            function istart(){
                
                document.getElementById("error").innerHTML = "";
                document.getElementById("winner").innerHTML = "";
                var xmlHttp = new XMLHttpRequest();
                xmlHttp.open( "POST", "<%out.print(response.encodeURL("http://localhost:8080/ttt/istart"));%>", false);
                xmlHttp.send();                
                
                updateBoard();
                document.getElementById("gameboard").style.display = "block";
            }
            
            function ustart(){
                document.getElementById("error").innerHTML = "";
                document.getElementById("winner").innerHTML = "";
                var xmlHttp = new XMLHttpRequest();
                xmlHttp.open( "POST", "<%out.print(response.encodeURL("http://localhost:8080/ttt/ustart"));%>", false);
                xmlHttp.send();                
                
                updateBoard();
                document.getElementById("gameboard").style.display = "block";
            }
            

            
        </script>
    </head>
    <body>
        <h1>Welcome to TicTacToe!</h1>
        <p>Click on a dash to place a cross</p>
        
        <h2 id="winner"></h2>

        <table id="gameboard">
            <tr>
              <td><a id="1" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x1y1"));%>', '1,1'); return false" href="#"></a></td>
              <td><a id="2" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x2y1"));%>', '2,1'); return false" href="#"></a></td>
              <td><a id="3" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x3y1"));%>', '3,1'); return false" href="#"></a></td>
            </tr>
            <tr>
              <td><a id="4" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x1y2"));%>', '1,2'); return false" href="#"></a></td>
              <td><a id="5" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x2y2"));%>', '2,2'); return false" href="#"></a></td>
              <td><a id="6" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x3y2"));%>', '3,2'); return false" href="#"></a></td>
            </tr>
            <tr>
              <td><a id="7" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x1y3"));%>', '1,3'); return false" href="#"></a></td>
              <td><a id="8" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x2y3"));%>', '2,3'); return false" href="#"></a></td>
              <td><a id="9" onclick="move('<%out.print(response.encodeURL("http://localhost:8080/ttt/move/x3y3"));%>', '3,3'); return false" href="#"></a></td>
            </tr>
        </table>
        <input id="istartb" type="button" value="New Game (I Start)" onclick="istart()" />
        <input id="ustartb" type="button" value="New Game (Computer Starts)" onclick="ustart()" />
        <p id="error"></p>
        <br>

        
    </body>
</html>