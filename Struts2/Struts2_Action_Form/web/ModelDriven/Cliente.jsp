<%-- 
    Document   : Cliente
    Created on : 24/10/2014, 03:37:40 PM
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
        <h1>Add Cliente!</h1>
        <s:form action="cliente-detalle">
            <s:textfield name="nombre" label="Nombre" />
            <s:textfield name="credito" label="Credito" />
            <s:submit value="Crear Cliente" />
        </s:form>
    </body>
</html>
