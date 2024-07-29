<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<jsp:include page="/components/header.jsp" />
<jsp:include page="/components/makerNavbar.jsp" />
<jsp:include page="/components/welcomeHeader.jsp"/>
<p style="text-align:right;"><span>
<a href="${pageContext.request.contextPath}/maker/register?lang=hi">Hindi</a>
<a href="${pageContext.request.contextPath}/maker/register?lang=en">English</a>
</span>
</p>
    <c:if test="${isExist==true}">
            <div class="container-fluid shadow-sm m-8 w-50 p-3 w-50 text-center mb-5 rounded alert alert-warning pt-6" role="alert">
            Customer Already Exist.
            </div>
    </c:if>
<jsp:include page="/components/registerForm.jsp" />
<jsp:include page="/components/footer.jsp" />