<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false"%>
                <div class="row m-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div id="testReportSection" class="col-9 m-0 p-2" style="width: 80%;">
                        <c:if test="${not empty successMessage}">
                            <div class="alert alert-success">${successMessage}</div>
                        </c:if>
                        <c:if test="${not empty errorMessage}">
                            <div class="alert alert-danger">${errorMessage}</div>
                        </c:if>
                        <div class="border border-gray shadow p-3 bg-white rounded">
                            <div class="p-2">
                                <h3 class="mb-2 text-center">Assigned LabTest Batches</h3>
                                <h4 class="mb-2 text-center text-green">${labTestName}</h4>
                                <a id="deleteLink" href="${pageContext.request.contextPath}/admin/deleteLabTest?id=${labTestId}"
                                type="button" class="py-2 px-3 rounded border shadow text-red text-sm"
                                onclick="return confirm ('Are you sure?')">
                                Delete LabTest
                             </a>
                                        <a href="${pageContext.request.contextPath}/admin/lab/assignTestLab1?id=${labTestId}"
                                            type="button"
                                            class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">
                                            Assign LabTest to batch
                                        </a>
                                        <br />
                                        <div class="p-3 mt-3">
                                            <table id="labsTable" class="table">
                                                <thead>
                                                    <tr>
                                                        <th scope="col">Batch Name</th>
                                                        <!-- <th scope="col">Status</th> -->
                                                        <th scope="col">Action</th>
                                                        <th scope="col">Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="assignedBatch" items="${assignedBatches}">
                                                        <tr>
                                                            <td>${assignedBatch.batchCode}</td>
                                                            <!-- <td>${status}</td> -->
                                                            <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                    href="${pageContext.request.contextPath}/admin/batchTests?id=${assignedBatch.id}&batchCode=${assignedBatch.batchCode}">View
                                                                    Reports</a></td>
                                                            <td>
                                                                <a class="py-2 px-3 rounded border bg-red text-white text-sm"
                                                                    href="${pageContext.request.contextPath}/admin/lab/editAssignLabTest?labTestId=${labTestId}&batchId=${assignedBatch.id}&labTestName=${labTestName}">Edit
                                                                    Assign LabTest</a>
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
                <script>
                    $(document).ready(function () {
                        $('#labsTable').DataTable();
                    });

           
    var assignedBatchesLength = "${assignedBatches}";
    function toggleDeleteLink() {
        var deleteLink = document.getElementById('deleteLink');

        if (assignedBatchesLength.length > 2) {
            deleteLink.style.display = 'none'; // Hide the delete link
        } else {
            deleteLink.style.display = 'inline-block'; // Show the delete link
        }
    }

    window.onload = toggleDeleteLink;

                </script>