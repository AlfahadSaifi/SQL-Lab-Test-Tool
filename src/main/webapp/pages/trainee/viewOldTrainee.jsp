<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>
<jsp:include page="/components/header.jsp" />
<jsp:include page="/components/adminNavbar.jsp" />
<link href="<c:url value='/resources/css/style.css' />" rel="stylesheet" />
<jsp:include page="/components/trainee/viewOldTrainee.jsp"/>
<jsp:include page="/components/footer.jsp" />
