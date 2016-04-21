<%-- 
    Document   : login
    Created on : 24/10/2014, 01:28:26 PM
    Author     : Elvis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style type="text/css">
            .errors {
                    background-color:#FFCCCC;
                    border:1px solid #CC0000;
                    width:400px;
                    margin-bottom:5px;
            }
            .errors li{ 
                    list-style: none; 
            }
        </style>
    </head>
    <body>
        <h1>Struts 2 ActionError &amp; ActionMessage Example</h1>
        <s:if test="hasActionErrors()">
            <div class="errors">
                <s:actionerror/>
            </div>
        </s:if>
        
        <s:form action="validacionUsuario">
            <s:textfield key="global.username" name="username" required="true"/>
            <s:password key="global.password" name="password"/>
            <s:submit key="global.submit" name="submit"/>
        </s:form>
    </body>
</html>
