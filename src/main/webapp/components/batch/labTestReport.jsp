<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/pdf-lib@1.16.0/dist/pdf-lib.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.0.1/css/buttons.dataTables.min.css">
</head>
<body>
    <div class="row m-0">
        <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
            <jsp:include page="/components/sideBar.jsp" />
        </div>
        <div id="testReportSection" class="col-9 m-0 p-2 " style="width: 80%;">
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
              </c:if>
              <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
              </c:if>
            <div class="border border-gray shadow p-3 bg-white rounded">
                <h3 class="mb-2 text-center">Lab Test Information</h3>
                <div class="row px-2">
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="card-title">Batch Name</h5>
                                <p class="card-text">${labTestReportPayload.batchCode}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="card-title">Lab Test Name</h5>
                                <p class="card-text">${labTestReportPayload.labTestName}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="card-title">Total Score</h5>
                                <p class="card-text">${labTestReportPayload.totalScore}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="card-title">Pass Percentage</h5>
                                <p class="card-text">${labTestReportPayload.passPercentage}</p>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="p-2">

                    <table id="myTable" class="table">
                        <thead>
                            <tr>
                                <th scope="col">Trainee ID</th>
                                <th scope="col">Trainee Name</th>
                                <th scope="col">LabTest Status</th>
                                <th scope="col">Score</th>
                                <th scope="col">Percentage</th>
                                <th scope="col">Result</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="testDetail" items="${labTestReportPayload.testDetails}">
                                <tr>
                                    <td>${testDetail.traineeId}</td>
                                    <td>${testDetail.traineeName}</td>
                                    <td>${testDetail.labTestStatus}</td>
                                    <td>${testDetail.obtainedScore}</td>
                                    <td>
                                        <fmt:formatNumber
                                            value="${(testDetail.obtainedScore / labTestReportPayload.totalScore) * 100}"
                                            maxFractionDigits="2" />
                                    </td>
                                    <td> ${(testDetail.obtainedScore / labTestReportPayload.totalScore)* 100
                                        >= labTestReportPayload.passPercentage ? '<span
                                            class="badge bg-success">Pass</span>'
                                        : '<span class="badge bg-danger">Fail</span>' }</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript" language="javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdn.datatables.net/buttons/2.0.1/js/dataTables.buttons.min.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/pdfmake.min.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.53/vfs_fonts.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdn.datatables.net/buttons/2.0.1/js/buttons.html5.min.js"></script>
<script type="text/javascript" language="javascript"
    src="https://cdn.datatables.net/buttons/2.0.1/js/buttons.print.min.js"></script>
<script>
    $(document).ready(function () {
        $('#myTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                {
                    extend: 'csv',
                    text: 'CSV',
                    filename: function () {
                        return '${labTestReportPayload.batchCode}' + '_' + '${labTestReportPayload.labTestName}';
                    },
                    exportOptions: {
                        columns: ':visible'
                    }
                },
                {
                    extend: 'pdfHtml5',
                    text: 'PDF',
                    filename: function () {
                        return '${labTestReportPayload.batchCode}' + '_' + '${labTestReportPayload.labTestName}';
                    },
                    title: 'Data Table PDF',
                    exportOptions: {
                        columns: ':visible'
                    }
                }
            ]
        });
    });
</script>
</html>