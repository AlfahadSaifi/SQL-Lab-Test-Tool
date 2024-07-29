<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ page isELIgnored="false" %>
            <div style="display:flex; margin-top:30px;">
                <div class="container-fluid col-2 shadow-sm bg-white rounded scrollable-content">
                    <a href="${pageContext.request.contextPath}/admin/batch">Add Batch</a><br>
                    <a href="${pageContext.request.contextPath}/admin/viewBatches">View Batches</a><br>
                    <a href="${pageContext.request.contextPath}/admin/lab">Add Lab</a><br>
                    <a href="${pageContext.request.contextPath}/admin/viewLabs">View Labs</a><br>
                    <a href="${pageContext.request.contextPath}/admin/lab/assignLab">Assign Labs</a><br>
                    <a href="${pageContext.request.contextPath}/admin/createLabTest">Create Lab Test</a><br>
                    <a href="${pageContext.request.contextPath}/admin/viewLabTest">View Lab Test</a><br>
                    <a href="${pageContext.request.contextPath}/admin/lab/assignTestLab">Assign Test Labs</a><br>
                </div>
