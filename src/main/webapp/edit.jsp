<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Edit</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<!--jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/-->
<form method="POST" action="meals" name="frmAddMeal">
    Id: <input type="number"  name="id" hidden
                     value=${meal.id} /> <br />
    DateTime: <input type="datetime-local"  name="dateTime" required
                     value=${meal.dateTime} /> <br />
    Description: <input type="text" name="description" required
                     value="${meal.description}" /> <br/>
    Calories: <input  type="number" name="calories" required
                     value="${meal.calories}" /> <br />
     <input type="submit" value="Submit" />
     <input type="submit" name="cancel" value="Cancel" />
</form>
</body>
</html>