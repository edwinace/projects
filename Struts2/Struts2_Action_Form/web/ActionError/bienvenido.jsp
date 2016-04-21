<%-- 
    Document   : bienvenido
    Created on : 24/10/2014, 01:28:34 PM
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
        <h1>Struts 2 Struts 2 ActionError &amp; ActionMessage Example</h1>
        <s:if test="hasActionMessages()">
            <div class="">
                <s:actionmessage/>
            </div>
        </s:if>
        
        <s:property value="getText('bienvenido.saludo')" /> : 
        <s:property value="username"/>
    </body>
</html>
