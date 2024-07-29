<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
                <%@ page isELIgnored="false" %>
                    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
                    <script
                        src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
                    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
                        rel="stylesheet">

                    <div class="row m-0">
                        <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                            <jsp:include page="/components/sideBar.jsp" />
                        </div>
                        <div id="testReportSection" class="col-9 m-0 p-2"
                            style="width: 80%; overflow-y: scroll; height: 540px;">
                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success">${successMessage}</div>
                            </c:if>
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger">${errorMessage}</div>
                            </c:if>
                            <div class="container shadow p-1 mb-2 bg-white rounded">
                                <div class="mt-1">
                                    <div class="row p-2">
                                        <div class="">
                                            <div class="card mb-1">
                                                <div class="card-header">
                                                    <b>Trainee Information</b>
                                                </div>
                                                <div class="card-body">
                                                    <div class="row">
                                                        <div class="col-4 m-0 d-flex gap-2"
                                                            style="flex-direction: column">
                                                            <span><b>Trainee ID:</b> ${traineeId}</span>
                                                            <span><b>Trainee Name:</b> ${traineeName}</span>
                                                            <span><b>Batch Name:</b> ${batchName}
                                                            </span>
                                                        </div>
                                                        <div class="col-4 m-0 d-flex gap-2"
                                                            style="flex-direction: column">
                                                            <span><b>Total Labs:</b> ${totalLabSize}</span>
                                                            <span><b>Total LabTests :</b> ${totalLabTestSize}</span>
                                                            <span><b>Total LabTests Passed:</b>
                                                                ${totalPassLabTest}</span>
                                                        </div>
                                                        <div class="col-4 m-0 d-flex gap-2"
                                                            style="flex-direction: column">
                                                            <div class="w-50">
                                                                <button
                                                                    class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                    onclick="downloadCSV()">CSV</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="container mt-1">
                                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                                        <li class="nav-item" role="presentation">
                                            <a class="nav-link active" id="labInfo-tab" data-bs-toggle="tab"
                                                href="#labInfo" role="tab" aria-controls="labInfo"
                                                aria-selected="true">Lab Information</a>
                                        </li>
                                        <li class="nav-item" role="presentation">
                                            <a class="nav-link" id="labTestInfo-tab" data-bs-toggle="tab"
                                                href="#labTestInfo" role="tab" aria-controls="labTestInfo"
                                                aria-selected="false">Lab Test Information</a>
                                        </li>
                                    </ul>
                                    <div class="tab-content" id="myTabContent">
                                        <div class="tab-pane fade show active" id="labInfo" role="tabpanel"
                                            aria-labelledby="labInfo-tab">
                                            <div class="p-3"
                                                style="border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                                                <table id="lab_table" class="table">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col">S.No.</th>
                                                            <th scope="col">Lab Name</th>
                                                            <th scope="col">Score</th>
                                                            <th scope="col">Percentage</th>
                                                            <th scope="col">Status</th>
                                                            <th scope="col">Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%! int labInfoCount=1; %>
                                                            <c:forEach var="lab" items="${lab}">
                                                                <tr>
                                                                    <td>
                                                                        <%= labInfoCount %>
                                                                    </td>
                                                                    <td>${lab.labName}</td>
                                                                    <td>${lab.obtainedMarks}</td>
                                                                    <td>
                                                                        <fmt:formatNumber
                                                                            value="${(lab.obtainedMarks / lab.totalMarks) * 100}"
                                                                            maxFractionDigits="2" />%
                                                                    </td>
                                                                    <td>
                                                                        ${lab.status}
                                                                    </td>
                                                                    <td>
                                                                        <a class="py-2 px-3 rounded border bg-blue text-white text-sm "
                                                                            href="${pageContext.request.contextPath}/admin/viewDetailedLabReports?traineeId=${traineeId}&labId=${lab.labId}&batchId=${batchId}&labName=${lab.labName}">Report</a>

                                                                    </td>
                                                                    <% labInfoCount++; %>
                                                                </tr>
                                                            </c:forEach>
                                                            <% labInfoCount=1; %>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                        <div class="tab-pane fade" id="labTestInfo" role="tabpanel"
                                            aria-labelledby="labTestInfo-tab">
                                            <div class="p-3"
                                                style="border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                                                <table id="lab_test_table" class="table">
                                                    <thead>
                                                        <tr>
                                                            <th scope="col">S.No.</th>
                                                            <th scope="col">Lab Test Name</th>
                                                            <th scope="col">Score</th>
                                                            <th scope="col">Percentage</th>
                                                            <th scope="col">Result</th>
                                                            <th scope="col">Status</th>
                                                            <th scope="col">Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <%! int labTestInfoCount=1; %>
                                                            <c:forEach var="labTest" items="${labTest}">
                                                                <tr>
                                                                    <td>
                                                                        <%= labTestInfoCount %>
                                                                    </td>
                                                                    <td>${labTest.labTestName}</td>
                                                                    <td>${labTest.obtainedMarks}</td>
                                                                    <td>
                                                                        <fmt:formatNumber
                                                                            value="${(labTest.obtainedMarks / labTest.totalLabTestPoints) * 100}"
                                                                            maxFractionDigits="2" />%
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when
                                                                                test="${(labTest.obtainedMarks/labTest.totalLabTestPoints)*100  >= labTest.passPercentage }">
                                                                                <span class="badge bg-success">
                                                                                    <i style="color: green;"></i> Pass
                                                                                </span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge bg-danger">
                                                                                    <i style="color: red;"></i> Fail
                                                                                </span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>

                                                                    <td>${labTest.labTestStatus}</td>

                                                                    <td>
                                                                        <a class="py-2 px-3 rounded border bg-blue text-white text-sm "
                                                                            href="${pageContext.request.contextPath}/admin/viewDetailedReports?traineeId=${traineeId}&labTestId=${labTest.labTestId}&batchId=${batchId}&labTestName=${labTest.labTestName}">Report</a>
                                                                    </td>
                                                                    <% labTestInfoCount++; %>
                                                                </tr>
                                                            </c:forEach>
                                                            <% labTestInfoCount=1; %>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    </div>
                    </div>
                    </div>

                    <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
                    <script>
                        $(document).ready(function () {
                            $('#lab_table').DataTable();
                            $('#lab_test_table').DataTable();
                        });

                        function downloadCSV() {
                            let traineeInfo = [
                                ["Trainee ID", "Trainee Name", "Batch Name", "Total Labs", "Total LabTests", "Total LabTests Passed"],
                                [
                                    document.querySelector('.col-4:nth-child(1) span:nth-child(1)').textContent.split(':')[1].trim(),
                                    document.querySelector('.col-4:nth-child(1) span:nth-child(2)').textContent.split(':')[1].trim(),
                                    document.querySelector('.col-4:nth-child(1) span:nth-child(3)').textContent.trim(),
                                    document.querySelector('.col-4:nth-child(2) span:nth-child(1)').textContent.split(':')[1].trim(),
                                    document.querySelector('.col-4:nth-child(2) span:nth-child(2)').textContent.split(':')[1].trim(),
                                    document.querySelector('.col-4:nth-child(2) span:nth-child(3)').textContent.split(':')[1].trim()
                                ]
                            ];

                            let labTableData = getTableData('lab_table');
                            let labTestTableData = getTableData('lab_test_table');

                            let combinedData = traineeInfo.concat(labTableData, labTestTableData);

                            let csvContent = 'data:text/csv;charset=utf-8,';
                            combinedData.forEach(function (row) {
                                csvContent += row.join(',') + '\n';
                            });

                            const encodedUri = encodeURI(csvContent);
                            const link = document.createElement('a');
                            link.setAttribute('href', encodedUri);
                            link.setAttribute('download', 'page_data.csv');
                            document.body.appendChild(link);
                            link.click();
                        }
                        function getTableData(tableId) {
                            let tableData = [];
                            const table = document.getElementById(tableId);
                            if (table) {
                                const rows = table.querySelectorAll('tr');
                                const headers = [];
                                const headerRow = table.querySelector('thead tr');
                                if (headerRow) {
                                    const headerCells = headerRow.querySelectorAll('th');
                                    for (let i = 0; i < headerCells.length; i++) {
                                        headers.push(headerCells[i].textContent.trim());
                                    }
                                    tableData.push(headers);
                                }
                                for (let i = 0; i < rows.length; i++) {
                                    const rowData = [];
                                    const cells = rows[i].querySelectorAll('td');
                                    for (let j = 0; j < cells.length; j++) {
                                        rowData.push(cells[j].textContent.trim());
                                    }
                                    tableData.push(rowData);
                                }
                            }
                            return tableData;
                        }

                    </script>