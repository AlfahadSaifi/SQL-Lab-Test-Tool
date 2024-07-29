<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value='/resources/js/index.js' />"></script>
<%@ page isELIgnored="false"%>
<h1 class="text-center mt-8 pt-5">Login</h1>
<div class="container-fluid m-8 w-50 shadow-sm p-3 mb-5 bg-white rounded">
    <div id="form1" class="m-5">
        <form action="login" method="post">
             <div class="mb-3">
                 <c:set var="error" value="${param.error}"/>
                 <c:if test="${error==true}">
                         <div class=" shadow-sm p-3 mb-5 rounded alert alert-danger pt-6" role="alert">
                         Invalid Username or Password.
                         </div>
                 </c:if>
             </div>
            <div class="mb-3">
                <label for="name" class="form-label">Username</label>
                <input type="text" class="form-control" name="username" id="name" placeholder="Enter your name" required>
            </div>
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" name="password" id="password" placeholder="Enter your password" required>
            </div>

            <div class="mb-3">
                <button type="submit" class="btn btn-primary">Login</button>
            </div>
        </form>
    </div>


</div>
