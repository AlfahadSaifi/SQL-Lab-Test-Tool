<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<nav id="sidebar" class="side-bar-position">
   <a href="${pageContext.request.contextPath}/trainee/dashboard">
       <div class="sidebar-header">
           <h3>Smart SQL Lab</h3>
        </div>
    </a> 
    <ul class="list-unstyled components px-1 d-none">
        <!-- <li class="active">
                <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">Home</a>
                <ul class="collapse list-unstyled" id="homeSubmenu">
                <li>
                    <a href="#">Home 1</a>
                </li>
                <li>
                    <a href="#">Home 2</a>
                </li>
                <li>
                    <a href="#">Home 3</a>
                </li>
                </ul>
                </li> -->
        <li><a href="${pageContext.request.contextPath}/trainee/dashboard">Dashboard</a></li>
        <li><a href="${pageContext.request.contextPath}/trainee/labs">Labs</a></li>
        <li><a href="${pageContext.request.contextPath}/trainee/labTests">Lab Test</a></li>
        <li><a href=""></a></li>
        <li><a href=""></a></li>
        <li><a href=""></a></li>
        <!-- <li><a href="${pageContext.request.contextPath}/trainee/viewLabs">View Labs</a></li> -->
        <!-- <li><a href="${pageContext.request.contextPath}/trainee/viewLabTest">View Lab Test</a></li> -->
        <!-- <li><a href="${pageContext.request.contextPath}/trainee/assignTestLab">Profile</a></li> -->
    </ul>
    <ul class="list-unstyled CTAs d-none">
        <li>
            <a href="${pageContext.request.contextPath}/logout" class="redColor bg-white text-md">Logout</a>
        </li>
    </ul>
</nav>