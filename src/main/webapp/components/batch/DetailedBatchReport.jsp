<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <%@ page isELIgnored="false" %>
                <div class="row m-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div class="col-9 m-0 p-2" style="width: 80%">
                        <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
                            <div class="my-3">
                                <div class="text-center main-heading">
                                    My Batches
                                </div>
                            </div>
                            <div class="row px-2">
                                <div class="col-md-3">
                                    <div class="card mb-4">
                                        <div class="card-body">
                                            <h5 class="card-title">Batch Name</h5>
                                            <p class="card-text">${batchInfo.batchCode}</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card mb-4">
                                        <div class="card-body">
                                            <h5 class="card-title">Total Trainees</h5>
                                            <p class="card-text">${batchInfo.totalTrainee}</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card mb-4">
                                        <div class="card-body">
                                            <h5 class="card-title">Total Labs</h5>
                                            <p class="card-text">${batchInfo.totalLabs}</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card mb-4">
                                        <div class="card-body">
                                            <h5 class="card-title">Total Lab Tests</h5>
                                            <p class="card-text">${batchInfo.totalLabTests}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="container mt-1">
                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                    <li class="nav-item" role="presentation">
                                        <a class="nav-link active" id="labInfo-tab" data-bs-toggle="tab" href="#labInfo"
                                            role="tab" aria-controls="labInfo" aria-selected="true">Lab Information</a>
                                    </li>
                                    <li class="nav-item" role="presentation">
                                        <a class="nav-link" id="labTestInfo-tab" data-bs-toggle="tab"
                                            href="#labTestInfo" role="tab" aria-controls="labTestInfo"
                                            aria-selected="false">Lab Test
                                            Information</a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">
                                    <div class="tab-pane fade show active" id="labInfo" role="tabpanel"
                                        aria-labelledby="labInfo-tab">
                                        <div class="p-3"
                                            style=" border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">

                                            <table id="lab_table" class="table">
                                                <thead>
                                                    <tr>
                                                        <th>S.No.</th>
                                                        <th>Lab Name</th>
                                                        <th>Lab Report</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <% count=1; %>
                                                        <c:forEach var="labInfo" items="${batchInfo.labPayloadList}">
                                                            <tr>
                                                                <td>
                                                                    <%= count %>
                                                                </td>
                                                                <td>${labInfo.labName}</td>
                                                                <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                        href="${pageContext.request.contextPath}/admin/labReport?labId=${labInfo.labId}&batchId=${batchId}&batchName=${batchInfo.batchCode}&labName=${labInfo.labName}">View
                                                                        Reports</a></td>
                                                            </tr>
                                                            <% count++; %>
                                                        </c:forEach>
                                                        <% count=1; %>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane fade" id="labTestInfo" role="tabpanel"
                                        aria-labelledby="labTestInfo-tab">
                                        <div class="p-3"
                                            style=" border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">

                                            <table id="lab_test_table" class="table">
                                                <thead>
                                                    <tr>
                                                        <th>S.No.</th>
                                                        <th>Lab Test Name</th>
                                                        <th>Lab Test Report</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <%! int count=1; %>
                                                        <c:forEach var="labTestInfo"
                                                            items="${batchInfo.labTestPayloadList}">
                                                            <tr>
                                                                <td>
                                                                    <%= count %>
                                                                </td>
                                                                <td>${labTestInfo.labTestName}</td>
                                                                <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                                        href="${pageContext.request.contextPath}/admin/labTestReport?labTestId=${labTestInfo.labTestId}&batchId=${batchId}&batchName=${batchInfo.batchCode}&labTestName=${labTestInfo.labTestName}">View
                                                                        Reports</a></td>
                                                            </tr>
                                                            <% count++; %>
                                                        </c:forEach>
                                                        <% count=1; %>
                                                </tbody>
                                            </table>
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
                </script>