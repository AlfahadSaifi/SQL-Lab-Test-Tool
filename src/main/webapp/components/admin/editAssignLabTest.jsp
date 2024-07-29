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
        <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
            <h3 class="mb-4 text-center">Edit Assign Test</h3>
            <form action="editAssignTest1" method="post">
                <div class="row g-3">
                    <!-- <div class="col-md-6"> -->
                        <input id="labTestId" type="text" name="labTestId" class="form-control d-none " required value="${labTestId}">
                        <input id="batchId" type="text" name="batchId" class="form-control d-none " required value="${batchId}">
                    <!-- </div> -->
                    <br />
                    <div class="col-md-6">
                        <label class="form-label">Lab Test Name</label>
                        <input id="labTestName" type="text" name="labTestName" class="form-control" required value="${labTestName}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Batch Code</label>
                        <input id="batchCode" type="text" name="batchCode" class="form-control" required value="${batchCode}">
                    </div>
                </div><br />
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Start Date</label>
                        <input id="startDate" type="datetime-local" name="startDate" class="form-control" required value="${labData.startDate}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">End Date</label>
                        <input id="endDate" type="datetime-local" name="endDate" class="form-control" required value="${labData.endDate}">
                    </div>
                </div>
                <br />
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Duration (in minutes)</label>
                        <input type="number" name="duration" class="form-control" required value="${labData.duration}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Passing Percentage</label>
                        <input type="text" name="passPercentage" id="passPercentage" class="form-control" required value="${labData.passPercentage}">
                        <small class="text-muted">Enter a number between 0 and 100 for the percentage.</small>
                    </div>
                </div>
                <div class="row g-3">
                    <div class="col-md-6">
                        <label class="form-label">Negative Marking Factor</label>
                        <input type="number" name="negativeMarkingFactor" class="form-control" required value="${labData.negativeMarkingFactor}">
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Assigned By</label>
                        <input id="assignedBy" type="text" name="assignedBy" class="form-control" required value="${labData.assignedBy}">
                    </div>
                </div>
                <div class="col-12 mx-auto mt-4 d-flex justify-content-end">
                    <button class="py-2 px-3 rounded border bg-blue text-white text-sm me-3" type="submit">Update Assign Test</button>
                    <a href="${pageContext.request.contextPath}/admin/lab/assignTestLab2?id=${labTestId}&labTestName=${labTestName}" class="py-2 px-3 rounded border bg-red text-white text-sm me-3">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</div>
</div>
</div>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script>
    // document.getElementById("batchCode").disabled = true;
    // document.getElementById("labTestName").disabled = true;
    // document.getElementById("assignedBy").disabled = true;
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');

    startDateInput.addEventListener('change', function() {
        endDateInput.min = this.value;
    });

    endDateInput.addEventListener('change', function() {
        startDateInput.max = this.value;
    });

   
</script>
