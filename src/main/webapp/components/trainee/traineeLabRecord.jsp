<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            <div class="border border-gray shadow p-3 bg-white rounded">
                <h3 class="mb-2 text-center">Trainee Lab Information</h3>
                <div class="row px-2">
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                                <h5 class="card-title">Batch Name</h5>
                                <p class="card-text">${batchName}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                                 <h5 class="card-title">Trainee ID</h5>
                                 <p class="card-text">${traineeId}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card mb-4">
                            <div class="card-body">
                            <h5 class="card-title">Trainee Name</h5>
                               <p class="card-text">${traineeName}</p>

                            </div>
                        </div>
                    </div>
                </div>
                <div class="p-2">
                    <table id="myTable" class="table">
                        <thead>
                            <tr>
                                <th scope="col">Serial No.</th>
                                <th scope="col">Lab Name</th>
                                <th scope="col">Score</th>
                                <th scope="col">Percentage</th>
                                <th scope="col">Lab Status</th>
                                <th scope="col">Action</th>

                            </tr>
                        </thead>
                        <tbody>
                        <%! int count = 1; %>
                             <c:forEach var="labDetail" items="${TraineeLabReport}">
                                                   <tr>
                                                      <td><%= count %></td>
                                                       <td>${labDetail.labName}</td>
                                                       <td>${labDetail.obtainedScore}</td>
                                                       <td>
                                                           <fmt:formatNumber
                                                               value="${(labDetail.obtainedScore / labDetail.totalScore) * 100}"
                                                     maxFractionDigits="2" />
                                           </td>
                                           <td>
                                               <script>
                                                   var labStatus = "${labDetail.labReports.labInfo.labStatus}";
                                                   if (labStatus == null || labStatus.trim() === "") {
                                                       document.write("Closed(Unattempted)");
                                                   } else {
                                                       document.write(labStatus);
                                                   }
                                               </script>
                                           </td>
                                            <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm " href="${pageContext.request.contextPath}/admin/viewDetailedLabReports?traineeId=${traineeId}&labId=${labDetail.labReports.labInfo.labId}&batchId=${batchId}&labName=${labDetail.labName}">View Detailed LabReports</a></td>


                                        <% count++; %>
                                  </tr>
                           </c:forEach>
                           <% count=1; %>
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
                        return '${labReportPayload.batchCode}' + '_' + '${labReportPayload.labName}';
                    },
                    exportOptions: {
                        columns: ':visible'
                    }
                },
                {
                    extend: 'pdfHtml5',
                    text: 'PDF',
                    filename: function () {
                        return '${labReportPayload.batchCode}' + '_' + '${labReportPayload.labName}';
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