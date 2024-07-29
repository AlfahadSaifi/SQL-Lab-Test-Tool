<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <div class="row m-0">
            <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                <jsp:include page="/components/sideBar.jsp" />
            </div>
            <div class="col-9 m-0 p-2" style="width: 80%">
                <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
                    <h3 class="text-center">
                        Batches Reports
                    </h3>
                    <div class="container mt-1">
                        <div class="p-3">
                            <table id="closedBatch" class="table">
                                <thead>
                                    <tr>
                                        <th scope="col">Batch Code</th>
                                        <th scope="col">Total Trainees</th>
                                        <th scope="col">Total Labs</th>
                                        <th scope="col">Total LabTests</th>
                                        <th scope="col">Reports</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="batchReport" items="${batchReports}">
                                        <tr>
                                            <td>${batchReport.batchCode}</td>
                                            <td>${batchReport.totalTrainees}</td>
                                            <td>${batchReport.totalLabs}</td>
                                            <td>${batchReport.totalLabTests}</td>
                                            <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                    href="${pageContext.request.contextPath}/admin/viewDetailedBatchReports?id=${batchReport.id}">View
                                                    Detailed Reports</a></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
        <script>
            $(document).ready(function () {
                $('#activeBatch').DataTable();
                $('#closedBatch').DataTable();
            });
        </script>