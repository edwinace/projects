<%-- 
    Document   : ClienteDetalle
    Created on : 24/10/2014, 03:37:48 PM
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
        <h1>Cliente Detalle!</h1>
        <p>
            Nombre: <s:property value="nombre" /> <br/>
            Credito: <s:property value="credito" />
        </p>
    </body>
</html>
