<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <jsp:include page="/components/header.jsp" />
        <jsp:include page="/components/adminNavbar.jsp" />
        <link href="<c:url value='/resources/css/style.css'  />" rel="stylesheet" />

        <div class="row m-0">
            <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                <jsp:include page="/components/sideBar.jsp" />
            </div>
            <div class="col-9 m-0 p-2" style="width: 80%;">
                <div class="">
                    <h2>Admin Access</h2>
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                    </c:if>
                </div>
            </div>
        </div>
        
<jsp:include page="/components/footer.jsp" />