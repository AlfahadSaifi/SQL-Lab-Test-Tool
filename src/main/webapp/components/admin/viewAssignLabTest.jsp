<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
    </div>
    <div class="col-9 m-0 p-2" style="width: 80%">
        <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
              </c:if>
              <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
              </c:if>
        <div class="border border-gray shadow p-3 bg-white rounded">
            <div class="p-2">
                <a href="${pageContext.request.contextPath}/admin/createLabTest" type="button"
                    class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">
                    Add New Lab Test
                </a>
                <h3 class="mb-4 text-center">View Lab Test</h3>
                <div class="container">
                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="assigned-tab" data-bs-toggle="tab"
                                data-bs-target="#assigned" type="button" role="tab" aria-controls="assigned"
                                aria-selected="true" onclick="handleTabClick('assigned')">Assigned</button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="unassigned-tab" data-bs-toggle="tab"
                                data-bs-target="#unassigned" type="button" role="tab"
                                aria-controls="unassigned" aria-selected="false"
                                onclick="handleTabClick('unassigned')">Unassigned</button>
                        </li>
                    </ul>
                    <div class="tab-content" id="myTabContent">
                        <div class="tab-pane fade show active" id="assigned" role="tabpanel"
                            aria-labelledby="assigned-tab">
                            <div class="p-3"
                                style=" border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                                <table id="labTestsTable" class="table">
                                    <thead>
                                        <tr>
                                            <th cope="col">Lab Test Name</th>
                                            <th cope="col">Actions 1</th>
                                            <th cope="col">Actions 2</th>
                                            <th cope="col">Actions 3</th>
                                        </tr>
                                    </thead>
                                    <tbody>

                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="tab-pane fade" id="unassigned" role="tabpanel"
                            aria-labelledby="unassigned-tab">
                            <div class="p-3"
                                style="border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                                <table id="labTestsTableUnAssigned" class="table">
                                    <thead>
                                        <tr>
                                            <th cope="col">Lab Test Name</th>
                                            <th cope="col">Actions 1</th>
                                            <th cope="col">Actions 2</th>
                                        </tr>
                                    </thead>
                                    <tbody>

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
<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>

<script>
    let assignedData = [];
    let unAssignedData = [];
    function handleTabClick(tabId) {
        document.querySelectorAll('.nav-link').forEach(tab => tab.classList.remove('active'));
        document.getElementById(tabId + '-tab').classList.add('active');
        document.querySelectorAll('.tab-pane').forEach(tabContent => {
            tabContent.classList.remove('show', 'active');
        });
        document.getElementById(tabId).classList.add('show', 'active');
        if (tabId == "assigned") {
            if (assignedData.length == 0){
                getAssigned();
            }
        } else if (tabId == "unassigned") {
            if (unAssignedData.length == 0)
                getUnAssigned();
        }
    }

    function getAssigned() {
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/admin/assignedLabTests",
            contentType: "application/json",
            success: function (data) {
                assignedData=data;
                generateAssigned(data);
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
    }

    function generateAssigned(data) {
        const tbody = document.querySelector('#labTestsTable tbody');
        tbody.innerHTML = '';
        data.forEach(test => {
            const row = document.createElement('tr');
            row.innerHTML =
                '<td>' + test.labTestName + '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewLabTestQuestions?id=' + test.labTestId + '" class="py-2 px-3 rounded border bg-blue text-white text-sm">View Questions</a>' +
                '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/lab/assignTestLab2?id=' + test.labTestId + '&labTestName=' + test.labTestName+'" class="py-2 px-3 rounded border bg-green text-white text-sm">Assign Lab Test</a>' +
                '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/labTest/viewLabTestReportByBatch?id=' + test.labTestId + '" class="py-2 px-3 rounded border bg-red text-white text-sm">View Report</a>' +
                '</td>';
            tbody.appendChild(row);
        });
        $('#labTestsTable').DataTable();

    }

    function getUnAssigned() {
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/admin/unAssignedLabTests",
            contentType: "application/json",
            success: function (data) {
                unAssignedData=data;
                generateUnAssigned(data);
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
        $('#labTestsTableUnAssigned').DataTable();
    }
    function generateUnAssigned(data) {
        const tbody = document.querySelector('#labTestsTableUnAssigned tbody');
        tbody.innerHTML = '';
        data.forEach(test => {
            const row = document.createElement('tr');
            row.innerHTML =
                '<td>' + test.labTestName + '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewLabTestQuestions?id=' + test.labTestId + '" class="py-2 px-3 rounded border bg-green text-white text-sm">View Questions</a>' +
                '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/lab/assignTestLab2?id=' + test.labTestId + '" class="py-2 px-3 rounded border bg-blue text-white text-sm">Assign Lab Test</a>' +
                '</td>' ;
                // +
                // '<td>' +
                // '<a href="${pageContext.request.contextPath}/admin/testReportSummary?id=' + test.labTestId + '" class="py-2 px-3 rounded border bg-red text-white text-sm">View Report</a>' +
                // '</td>';
            tbody.appendChild(row);
        });
    }


    const urlParams = new URLSearchParams(window.location.search);
    const activeTab = urlParams.get('tab');
    if (activeTab) {
        handleTabClick(activeTab);
    } else {
        handleTabClick("assigned");
    }


</script>