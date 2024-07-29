<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<html>

<head>
</head>

<body>
    <div style="display:flex; margin-left:10px; margin-top:30px;">
        <div class="container-fluid col-2 shadow-sm bg-white rounded scrollable-content">
            <h3>Your Labs</h3>
            <ol>
                <c:forEach var="e" items="${labs}">
                    <li><a
                            href="${pageContext.request.contextPath}/trainee/?id=${e.labId}">${e.labName}</a>
                    </li>
                </c:forEach>
            </ol>
        </div>
            <div class="container-fluid col-2 shadow-sm bg-white rounded scrollable-content">
                <h3>Your Tests</h3>
                <ol>
                    <c:forEach var="e" items="${tests}">
                        <li><a href="${pageContext.request.contextPath}/trainee/?testId=${e.labId}"
                                class="test-link">${e.labName}</a></li>
                    </c:forEach>
                </ol>
            </div>
</body>

</html>