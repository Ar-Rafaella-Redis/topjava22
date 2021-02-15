<%--
  Created by IntelliJ IDEA.
  User: Anna
  Date: 11.02.2021
  Time: 0:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html>
<head>
    <title>Meals</title>
    <style>
    .normal{color:green;}
    .exceeded{color:red;}
    </style>
</head>
<h3><a href="index.html">Home</a></h3>
<body>
<p><a href="edit.jsp?action=insert">Add meal</a></p>
<div align="left">
    <table border="1" cellpadding="4">
        <caption><h2>List of meals</h2></caption>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <%--@elvariable id="meals" type="java.util.List"--%>
         <c:forEach items="${meals}" var="meal" >
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr class="${meal.excess? 'exceeded' : 'normal'}">
                <td><fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/></td>
                <td><c:out value="${meal.description}" /></td>
                <td><c:out value="${meal.calories}" /></td>
                <td><a href="meals?action=edit&mealId=<c:out value="${meal.id}"/>">Update</a></td>
                <td><a href="meals?action=delete&mealId=<c:out value="${meal.id}"/>">Delete</a></td>
            </tr>
         </tr>
        </c:forEach>
    </table>
</div>


</body>
</html>
