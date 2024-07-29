<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>
                <div class="">
                    <div class="row m-0">
                        <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                            <jsp:include page="/components/sideBar.jsp" />
                        </div>
                        <div id="testReportSection" class="col-9 m-0 p-2" style="width: 80%;">
                            <div class="border border-gray shadow p-3 bg-white rounded">
                                <div class="p-2">
                                    <h3 class="mb-2 text-center">Lab Batches</h3>
                                    <br />
                                    <div class="p-3 mt-3">
                                        <table id="labsTable" class="table">
                                            <thead>
                                                <tr>
                                                    <th scope="col">Lab Name</th>
                                                    <th scope="col">Batch Name</th>
                                                    <th scope="col">Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="assignedBatch" items="${assignedBatches}">
                                                    <tr>
                                                        <td>${labName}</td>
                                                        <td>${assignedBatch.batchCode}</td>
                                                        <td> <a href="${pageContext.request.contextPath}/admin/viewReports?labId=${labId}&batchId=${assignedBatch.id}"
                                                                class="py-2 px-3 rounded border bg-red text-white text-sm">View
                                                                Reports</a>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
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