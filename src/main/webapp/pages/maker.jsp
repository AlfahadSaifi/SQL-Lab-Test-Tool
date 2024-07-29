<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<jsp:include page="/components/header.jsp" />
<jsp:include page="/components/makerNavbar.jsp" />
<jsp:include page="/components/welcomeHeader.jsp"/>

<div class="container -fluid m-3 mt-5">
    <a href="${pageContext.request.contextPath}/maker/register" class="btn btn-primary">Add Customer</a>
</div>
    <c:if test="${isFound==false}">
            <div class="container-fluid shadow-sm m-8 w-50 p-3 w-50 text-center mb-5 rounded alert alert-warning pt-6" role="alert">
            Customer Not Found.
            </div>
    </c:if>
    <c:set var="isFound" value="${null}"/>
<jsp:include page="/components/makerTable.jsp" />
<jsp:include page="/components/footer.jsp" />
