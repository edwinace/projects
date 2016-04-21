<%-- 
    Document   : login
    Created on : 24/10/2014, 01:05:14 PM
    Author     : Elvis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <s:form action="bienvenido">
            <s:textfield name="username" label="Username" />
            <s:submit value="Iniciar Sesion" />
        </s:form>
    </body>
</html>
