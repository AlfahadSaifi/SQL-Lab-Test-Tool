<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<nav id="sidebar" class="side-bar-position">
    <div class="sidebar-header">
        <h3>Smart SQL Lab</h3>
    </div>
    <ul class="list-unstyled components px-1">
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
        <!-- <li><a href="${pageContext.request.contextPath}/admin/batch">Add Batch</a> </li> -->
        <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <li><a href="${pageContext.request.contextPath}/admin/viewBatches">Batches</a>
        <!-- <li><a href="${pageContext.request.contextPath}/admin/lab">Add Lab</a></li> -->
        <!-- <li><a href="${pageContext.request.contextPath}/admin/viewLabs1">View Labs</a></li> -->
        <li><a href="${pageContext.request.contextPath}/admin/viewAssignLab">Assignments</a></li>
        <!-- <li><a href="${pageContext.request.contextPath}/admin/createLabTest">Create Lab Test</a></li> -->
        <!-- <li><a href="${pageContext.request.contextPath}/admin/viewLabTest">View Lab Test</a></li> -->
        <li><a href="${pageContext.request.contextPath}/admin/viewAssignLabTest">Assessments</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/viewQuestionBank">Question Bank</a></li>
        <!-- <li><a href="${pageContext.request.contextPath}/admin/lab/assignLab">Assign Labs</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/lab/assignTestLab">Assign Labs Test</a></li> -->
         <!-- <li><a href="${pageContext.request.contextPath}/admin/lab/deassignTestLab">DeAssign Test Labs</a></li> -->
        <li><a href="${pageContext.request.contextPath}/admin/reports">Reports</a></li>
    </ul>
    <ul class="list-unstyled CTAs">
        <li>
            <a href="${pageContext.request.contextPath}/logout" class="redColor bg-white text-md">Logout</a>
        </li>
    </ul>
</nav>