<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
    <jsp:include page="/components/sideBar.jsp" />
    </div>
    <div class="col-9 m-0 p-2"  style="width: 80%">
    <div class="container-fluid shadow p-3 mb-5 bg-white rounded">

        <div class="text-center mt-4 main-heading">
        Lab Test Reports
        </div>
        <div class="container m-1">
            <table id="myTable" class="table">
                <thead>
                    <tr>
                        <th scope="col">Trainee Id</th>
                        <th scope="col">Employee Name</th>
                        <th scope="col">Batch Name</th>
                        <th scope="col">LabTest Name</th>
                        <th scope="col">Total Question</th>
                        <th scope="col">Correct</th>
                        <th scope="col">Incorrect</th>
                        <th scope="col">Skipped</th>
                        <th scope="col">Result</th>
                        <th scope="col">Report</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="e" items="${labReport}">
                        <tr>
                            <td>${e.employeeId}</td>
                            <td>${e.employeeName}</td>
                            <td>${e.batchName}</td>
                            <td>${e.labTestName}</td>
                            <td>${e.totalQuestions}</td>
                            <td>${e.correctQuestions}</td>
                            <td>${e.incorrectQuestions}</td>
                            <td>${e.skippedQuestions}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${e.result eq 'PASS'}">
                                        <span style="color: #4CAF50; font-weight: bold;">${e.result}</span>
                                    </c:when>
                                    <c:when test="${e.result eq 'FAIL'}">
                                        <span style="color: #FF5722; font-weight: bold;">${e.result}</span>
                                    </c:when>
                                    <c:otherwise>${e.result}</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                    href="${pageContext.request.contextPath}/admin/viewDetailedReports?id=${e.batchId}&id1=${e.labTestId}&id2=${e.employeeId}">Report</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <br /><br />
        </div>
    </div>
</div>
</div>

</body>
</html>

<script>

$(document).ready(function () {

    $('#myTable').DataTable({
        dom: 'Bfrtip',
        buttons: ['csv', 'pdf']
    });

    function toggleSidebar() {
        var sidebar = $("#sidebar");
        var testReportSection = $("#testReportSection");

        if (sidebar.is(":visible")) {
            sidebar.hide();
            testReportSection.css("width", "100%");
        } else {
            sidebar.show();
            testReportSection.css("width", "80%");
        }
    }

    $("#toggleSidebarButton").click(toggleSidebar);
});
</script>