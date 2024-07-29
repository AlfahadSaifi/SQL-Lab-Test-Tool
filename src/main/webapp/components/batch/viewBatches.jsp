<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <a href="${pageContext.request.contextPath}/admin/batch" type="button"
            class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">
            Add New Batch
        </a>
        <div class="text-center main-heading">
            My Batches
        </div>
        <div class="container">
            <ul class="nav nav-tabs" id="myTab" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="active-tab" data-bs-toggle="tab" data-bs-target="#active"
                        type="button" role="tab" aria-controls="active" aria-selected="false"
                        onclick="handleTabClick('active')">Active</button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="closed-tab" data-bs-toggle="tab"
                        data-bs-target="#closed" type="button" role="tab" aria-controls="closed"
                        aria-selected="true" onclick="handleTabClick('closed')">Closed</button>
                </li>
            </ul>
            <div class="tab-content" id="myTabContent">
                <div class="tab-pane fade show active" id="active" role="tabpanel"
                    aria-labelledby="active-tab">
                    <div class="p-3"
                        style="border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                        <table id="activeBatch" class="table">
                            <thead>
                                <tr>
                                    <th scope="col">Batch Code</th>
                                    <th scope="col">Action 1</th>
                                    <th scope="col">Action 2</th>
                                    <th scope="col">Action 3</th>
                                </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane fade" id="closed" role="tabpanel" aria-labelledby="closed-tab">
                    <div class="p-3"
                        style="border: 1px solid #e5dbdb; border-top: 1px solid white; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;">
                        <table id="closedBatch" class="table">
                            <thead>
                                <tr>
                                    <th scope="col">Batch Code</th>
                                    <th scope="col">Action 1</th>
                                    <th scope="col">Action 2</th>
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

<script src="https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js"></script>
<script>
    let closedData = [];
    let activeData = [];
    function handleTabClick(tabId) {
        document.querySelectorAll('.nav-link').forEach(tab => tab.classList.remove('active'));
        document.getElementById(tabId + '-tab').classList.add('active');
        document.querySelectorAll('.tab-pane').forEach(tabContent => {
            tabContent.classList.remove('show', 'active');
        });
        document.getElementById(tabId).classList.add('show', 'active');
        if (tabId == "active") {
            if (activeData.length == 0) {
                getActive();
            }
        } else if (tabId == "closed") {
            if (closedData.length == 0)
                getclosed();
        }
    }

    function getclosed() {
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/admin/closedBatches",
            contentType: "application/json",
            success: function (data) {
                closedData = data;
                generateClosed(data);
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
    }

    function generateClosed(data) {
        const tbody = document.querySelector('#closedBatch tbody');
        tbody.innerHTML = '';
        data.forEach(test => {
            const row = document.createElement('tr');
            row.innerHTML =
            '<td>' + test.batchCode + '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewOldTrainee?id=' + test.batchId + '" class="py-2 px-3 rounded border bg-green text-white text-sm">View Trainee</a>' +
                '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewDetailedBatchReports?id=' + test.batchId + '" class="py-2 px-3 rounded border bg-blue text-white text-sm">View Report</a>' +
                '</td>'
                ;
            tbody.appendChild(row);
        });
        $('#closedBatch').DataTable();
    }

    function getActive() {
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/admin/activeBatches",
            contentType: "application/json",
            success: function (data) {
                unclosedData = data;
                generateActive(data);
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });
    }
    function generateActive(data) {
        const tbody = document.querySelector('#activeBatch tbody');
        tbody.innerHTML = '';
        data.forEach(test => {
            const row = document.createElement('tr');
            row.innerHTML =
            '<td>' + test.batchCode + '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewTrainee?id=' + test.batchId + '" class="py-2 px-3 rounded border bg-green text-white text-sm">View Trainee</a>' +
                '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewLabs?batchId=' + test.batchId + '" class="py-2 px-3 rounded border bg-blue text-white text-sm">View Lab</a>' +
                '</td>' +
                '<td>' +
                '<a href="${pageContext.request.contextPath}/admin/viewLabTest?batchId=' + test.batchId + '" class="py-2 px-3 rounded border bg-blue text-white text-sm">View Lab Test</a>' +
                '</td>'
                // +
                // '<td>' +
                // '<a href="${pageContext.request.contextPath}/admin/viewLabToShowReport?id=' + test.batchId + '" class="py-2 px-3 rounded border bg-red text-white text-sm">View Reports</a>' +
                // '</td>'
                ;
            tbody.appendChild(row);
        });
        $('#activeBatch').DataTable();
  
    }


    const urlParams = new URLSearchParams(window.location.search);
    const activeTab = urlParams.get('tab');
    if (activeTab) {
        handleTabClick(activeTab);
    } else {
        handleTabClick("active");
    }


</script>