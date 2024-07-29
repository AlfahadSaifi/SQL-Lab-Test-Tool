<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>
                <div class="row m-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div id="testReportSection" class="col-9 m-0 p-2" style="width: 80%;">
                        <div class="border border-gray shadow p-3 bg-white rounded">
                            <div class="p-2">
                                <h3 class="mb-2 text-center">Report LabTest Batches</h3>
                                <br />
                                <div class="p-3 mt-3">
                                    <table id="labsTable" class="table">
                                        <thead>
                                            <tr>                                            
                                                <th scope="col">S.No.</th>
                                                <th scope="col">Batch Name</th>
                                                <th scope="col">Action</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="counter" value="1" />
                                            <c:forEach var="assignedBatch" items="${assignedBatches}">
                                                <tr>                                 
                                                    <td >${counter}</td>                 
                                                    <td>${assignedBatch.batchCode}</td>
                                                    <td><a class="py-2 px-3 rounded border bg-green text-white text-sm"
                                                        href="${pageContext.request.contextPath}/admin/testReportSummary?id=${labTestId}&batchId=${assignedBatch.id}&batchName=${assignedBatch.batchCode}&labTestName=${labTestName}">View
                                                        Reports</a></td>                                        
                                                </tr>
                                                <c:set var="counter" value="${counter + 1}" />
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <script>
                    $(document).ready(function () {
                        $('#labsTable').DataTable();
                    });
                </script>