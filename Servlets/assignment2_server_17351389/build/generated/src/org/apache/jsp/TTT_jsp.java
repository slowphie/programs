package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import javax.servlet.http.*;

public final class TTT_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>TicTacToe</title>\n");
      out.write("        <style>\n");
      out.write("            table {\n");
      out.write("                border-collapse: collapse;\n");
      out.write("                display: none;\n");
      out.write("                \n");
      out.write("            }\n");
      out.write("\n");
      out.write("            table, th, td {\n");
      out.write("                border: 1px solid black;\n");
      out.write("                padding: 30px 20px;\n");
      out.write("                font-size: 30px;\n");
      out.write("        \n");
      out.write("            }\n");
      out.write("\n");
      out.write("            td:hover {\n");
      out.write("                background-color: lightsteelblue;\n");
      out.write("            }\n");
      out.write("\n");
      out.write("            a{\n");
      out.write("                text-decoration: none;\n");
      out.write("                padding: 40px 20px;\n");
      out.write("            }\n");
      out.write("            \n");
      out.write("            #winner {\n");
      out.write("                color: green;\n");
      out.write("            }\n");
      out.write("            #error {\n");
      out.write("                color: red;\n");
      out.write("            }\n");
      out.write("        </style>\n");
      out.write("        <script type=text/javascript\"></script>\n");
      out.write("        <script>\n");
      out.write("            window.onload = function() {\n");
      out.write("            updateBoard();\n");
      out.write("            var xmlHttpWon1 = new XMLHttpRequest();\n");
      out.write("                xmlHttpWon1.open( \"GET\", \"");
out.print(response.encodeURL("http://localhost:8080/ttt/won"));
      out.write("\", false);\n");
      out.write("                xmlHttpWon1.send();\n");
      out.write("                if(!xmlHttpWon1.responseText.includes('none')){                        \n");
      out.write("                    if (!xmlHttpWon1.responseText.includes('draw')){\n");
      out.write("                        document.getElementById(\"winner\").innerHTML = \"Winner: \" + xmlHttpWon1.responseText;\n");
      out.write("                    } else {\n");
      out.write("                        document.getElementById(\"winner\").innerHTML = \"No winner game is a \" + xmlHttpWon1.responseText;\n");
      out.write("\n");
      out.write("                    }\n");
      out.write("\n");
      out.write("                }\n");
      out.write("\n");
      out.write("                document.getElementById(\"gameboard\").style.display = \"block\";\n");
      out.write("                \n");
      out.write("            }\n");
      out.write("            function updateBoard(){\n");
      out.write("                    \n");
      out.write("                    var xmlHttp = new XMLHttpRequest();\n");
      out.write("                    xmlHttp.open( \"GET\", \"");
out.print(response.encodeURL("http://localhost:8080/ttt/state?format=txt"));
      out.write("\", false);\n");
      out.write("                    xmlHttp.send();\n");
      out.write("                    \n");
      out.write("                    var board= xmlHttp.responseText;\n");
      out.write("                    board = board.substring(0,3)+board.substring(4,7)+board.substring(8);\n");
      out.write("                    \n");
      out.write("                    for (var i = 1; i<10; i++){\n");
      out.write("                        document.getElementById(i.toString()).innerHTML = board.charAt(i-1);\n");
      out.write("                    }\n");
      out.write("                    \n");
      out.write("  \n");
      out.write("                }\n");
      out.write("                \n");
      out.write("            function move(url, pos){\n");
      out.write("                document.getElementById(\"error\").innerHTML = \"\";\n");
      out.write("                var won = document.getElementById(\"winner\").innerHTML;\n");
      out.write("                if(!won){ \n");
      out.write("                    var xmlHttp = new XMLHttpRequest();\n");
      out.write("                    xmlHttp.open( \"GET\", \"");
out.print(response.encodeURL("http://localhost:8080/ttt/possiblemoves"));
      out.write("\", false);\n");
      out.write("                    xmlHttp.send();\n");
      out.write("                    \n");
      out.write("                    if(xmlHttp.responseText.includes(pos)){                \n");
      out.write("                        var xmlHttpMove = new XMLHttpRequest();\n");
      out.write("                        xmlHttpMove.open( \"POST\", url, false);\n");
      out.write("                        xmlHttpMove.send();\n");
      out.write("                        \n");
      out.write("                        if(xmlHttpMove.status == 200){\n");
      out.write("\n");
      out.write("                            var xmlHttpWon1 = new XMLHttpRequest();\n");
      out.write("                            xmlHttpWon1.open( \"GET\", \"");
out.print(response.encodeURL("http://localhost:8080/ttt/won"));
      out.write("\", false);\n");
      out.write("                            xmlHttpWon1.send();\n");
      out.write("                            if(!xmlHttpWon1.responseText.includes('none')){                        \n");
      out.write("                                if (!xmlHttpWon1.responseText.includes('draw')){\n");
      out.write("                                    document.getElementById(\"winner\").innerHTML = \"Winner: \" + xmlHttpWon1.responseText;\n");
      out.write("                                } else {\n");
      out.write("                                    document.getElementById(\"winner\").innerHTML = \"No winner game is a \" + xmlHttpWon1.responseText;\n");
      out.write("\n");
      out.write("                                }\n");
      out.write("\n");
      out.write("                            }\n");
      out.write("\n");
      out.write("                            updateBoard();\n");
      out.write("                        }\n");
      out.write("\n");
      out.write("                    } else {\n");
      out.write("                        console.log(xmlHttp.responseText);\n");
      out.write("                        document.getElementById(\"error\").innerHTML = \"Invalid move\";\n");
      out.write("                    }\n");
      out.write("                } else {\n");
      out.write("                    document.getElementById(\"error\").innerHTML = \"Game is finished.\";\n");
      out.write("                }\n");
      out.write("            }\n");
      out.write("            \n");
      out.write("            function istart(){\n");
      out.write("                \n");
      out.write("                document.getElementById(\"error\").innerHTML = \"\";\n");
      out.write("                document.getElementById(\"winner\").innerHTML = \"\";\n");
      out.write("                var xmlHttp = new XMLHttpRequest();\n");
      out.write("                xmlHttp.open( \"POST\", \"");
out.print(response.encodeURL("http://localhost:8080/ttt/istart"));
      out.write("\", false);\n");
      out.write("                xmlHttp.send();                \n");
      out.write("                \n");
      out.write("                updateBoard();\n");
      out.write("                document.getElementById(\"gameboard\").style.display = \"block\";\n");
      out.write("            }\n");
      out.write("            \n");
      out.write("            function ustart(){\n");
      out.write("                document.getElementById(\"error\").innerHTML = \"\";\n");
      out.write("                document.getElementById(\"winner\").innerHTML = \"\";\n");
      out.write("                var xmlHttp = new XMLHttpRequest();\n");
      out.write("                xmlHttp.open( \"POST\", \"");
out.print(response.encodeURL("http://localhost:8080/ttt/ustart"));
      out.write("\", false);\n");
      out.write("                xmlHttp.send();                \n");
      out.write("                \n");
      out.write("                updateBoard();\n");
      out.write("                document.getElementById(\"gameboard\").style.display = \"block\";\n");
      out.write("            }\n");
      out.write("            \n");
      out.write("\n");
      out.write("            \n");
      out.write("        </script>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        <h1>Welcome to TicTacToe!</h1>\n");
      out.write("        <p>Click on a dash to place a cross</p>\n");
      out.write("        \n");
      out.write("        <h2 id=\"winner\"></h2>\n");
      out.write("\n");
      out.write("        <table id=\"gameboard\">\n");
      out.write("            <tr>\n");
      out.write("              <td><a id=\"1\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x1y1"));
      out.write("', '1,1'); return false\" href=\"#\"></a></td>\n");
      out.write("              <td><a id=\"2\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x2y1"));
      out.write("', '2,1'); return false\" href=\"#\"></a></td>\n");
      out.write("              <td><a id=\"3\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x3y1"));
      out.write("', '3,1'); return false\" href=\"#\"></a></td>\n");
      out.write("            </tr>\n");
      out.write("            <tr>\n");
      out.write("              <td><a id=\"4\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x1y2"));
      out.write("', '1,2'); return false\" href=\"#\"></a></td>\n");
      out.write("              <td><a id=\"5\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x2y2"));
      out.write("', '2,2'); return false\" href=\"#\"></a></td>\n");
      out.write("              <td><a id=\"6\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x3y2"));
      out.write("', '3,2'); return false\" href=\"#\"></a></td>\n");
      out.write("            </tr>\n");
      out.write("            <tr>\n");
      out.write("              <td><a id=\"7\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x1y3"));
      out.write("', '1,3'); return false\" href=\"#\"></a></td>\n");
      out.write("              <td><a id=\"8\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x2y3"));
      out.write("', '2,3'); return false\" href=\"#\"></a></td>\n");
      out.write("              <td><a id=\"9\" onclick=\"move('");
out.print(response.encodeURL("http://localhost:8080/ttt/move/x3y3"));
      out.write("', '3,3'); return false\" href=\"#\"></a></td>\n");
      out.write("            </tr>\n");
      out.write("        </table>\n");
      out.write("        <input id=\"istartb\" type=\"button\" value=\"New Game (I Start)\" onclick=\"istart()\" />\n");
      out.write("        <input id=\"ustartb\" type=\"button\" value=\"New Game (Computer Starts)\" onclick=\"ustart()\" />\n");
      out.write("        <p id=\"error\"></p>\n");
      out.write("        <br>\n");
      out.write("\n");
      out.write("        \n");
      out.write("    </body>\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
