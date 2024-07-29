<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <html>

            <head>
                <script src="https://cdn.jsdelivr.net/npm/pdf-lib@1.16.0/dist/pdf-lib.js"></script>
                <link rel="stylesheet" href="https://cdn.datatables.net/buttons/2.0.1/css/buttons.dataTables.min.css" />
            </head>

            <body>
                <div class="row m-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div id="testReportSection" class="col-9 m-0 p-2" style="width: 80%">
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
                                            <p class="card-text">
                                                ${labTestReportPayload.passPercentage}
                                            </p>
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
                                            <th scope="col">Report</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="e" items="${labTestReportPayload.testDetails}">
                                            <tr>
                                                <td>${e.traineeId}</td>
                                                <td>${e.traineeName}</td>
                                                <td>${e.labTestStatus}</td>
                                                <td>${e.obtainedScore}</td>
                                                <td>
                                                    <fmt:formatNumber
                                                        value="${(e.obtainedScore / labTestReportPayload.totalScore) * 100}"
                                                        maxFractionDigits="2" />
                                                </td>
                                                <td>
                                                    ${(e.obtainedScore / labTestReportPayload.totalScore)* 100
                                                    >= labTestReportPayload.passPercentage ? '<span
                                                        class="badge bg-success">Pass</span>' : '<span
                                                        class="badge bg-danger">Fail</span>' }
                                                </td>

                                                <td id="detailedReportButton_${e.traineeId}"></td>
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
                    $("#myTable").DataTable({
                        dom: "Bfrtip",
                        buttons: [
                            {
                                extend: "csv",
                                text: "CSV",
                                filename: function () {
                                    return (
                                        "${labTestReportPayload.batchCode}" +
                                        "_" +
                                        "${labTestReportPayload.labTestName}"
                                    );
                                },
                                exportOptions: {
                                    columns: ":visible",
                                },
                            },
                            {
                                extend: "pdfHtml5",
                                text: "PDF",
                                filename: function () {
                                    return (
                                        "${labTestReportPayload.batchCode}" +
                                        "_" +
                                        "${labTestReportPayload.labTestName}"
                                    );
                                },
                                title: "Data Table PDF",
                                exportOptions: {
                                    columns: ":visible",
                                },
                            },
                        ],
                    });
                });
            </script>

            <script>
                <c:forEach var="e" items="${labTestReportPayload.testDetails}">
                    document.getElementById('detailedReportButton_${e.traineeId}').innerHTML =
                    `<button
                        class="py-2 px-3 rounded border bg-blue text-white text-sm"
                        onclick="postRequest('${pageContext.request.contextPath}/admin/viewDetailedReports', [
                    { name: 'traineeId', value: '${e.traineeId}' },
                { name: 'labTestId', value: '${labTestId}' },
                { name: 'batchId', value: '${batchId}' },
                { name: 'labTestName', value: '${labTestName}' }
                    ])"
                    >
                        Detailed Report
                    </button>
      `;
    </c:forEach>;
            </script>

            <script>
                function postRequest(url, paramsArray) {
                    var form = document.createElement("form");
                    form.action = url;
                    form.method = "post";

                    paramsArray.forEach(function (param) {
                        var input = document.createElement("input");
                        input.type = "hidden";
                        input.name = param.name;
                        input.value = param.value;
                        form.appendChild(input);
                    });

                    document.body.appendChild(form);
                    form.submit();

                    document.body.removeChild(form);
                }
            </script>

            </html>