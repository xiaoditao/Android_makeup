<%-- 
    Document   : dashboard
    Created on : Apr 4, 2019, 11:51:55 AM
    Author     : xiaoditao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>dashboard</title>
    </head>
    <body>
        <h2>Please type this url to see the dashboard: http://localhost:8080/Project4Task2/dashboard</h2>
        <h3>Operations analytics:</h3>
        <p>Count of the logs: <%=request.getAttribute("count")%> </p><br>
        <p>Average search time latency: <%=request.getAttribute("average")%> milliseconds</p><br>
        <h3>The following shows the logs:</h3>
        <form action="dashboard" method="GET">
        <p><%=request.getAttribute("answer")%> </p>  
        </form>
    </body>
</html>

