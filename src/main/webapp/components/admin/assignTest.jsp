<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>
                <div class="row m-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div class="col-9 m-0 p-2" style="width: 80%;">
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
                        <div class="container-fluid p-5 pt-4 shadow mb-2 bg-white rounded">
                            <h3 class="text-center mb-2">Assign Test</h3>
                            <form action="assignTest" method="post">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="labTestId" class="form-label">Select a Test<span
                                                class="text-danger ms-1">*</span></label>
                                        <select name="labTestId" id="labTestId" class="form-select form-select-sm"
                                            required>
                                            <option value="">-- Select a Test --</option>
                                            <c:forEach var="e" items="${tests}">
                                                <option value="${e.labTestId}"
                                                    data-passing-percentage="${e.passPercentage}">
                                                    ${e.labTestName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="batch" class="form-label">Select a Batch<span
                                                class="text-danger ms-1">*</span></label>
                                        <select name="batch" id="batch" class="form-select form-select-sm" required>
                                            <option value="">-- Select a Batch --</option>
                                            <c:forEach var="e" items="${batches}">
                                                <option value="${e.id}">${e.batchCode}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="startDate" class="form-label">Start Date<span
                                                class="text-danger ms-1">*</span></label>
                                        <input id="startDate" type="datetime-local" name="startDate"
                                            class="form-control" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="endDate" class="form-label">End Date<span
                                                class="text-danger ms-1">*</span></label>
                                        <input id="endDate" type="datetime-local" name="endDate" class="form-control"
                                            required>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label for="duration" class="form-label">Duration (in minutes)<span
                                                class="text-danger ms-1">*</span></label>
                                        <input type="number" name="duration" id="duration" class="form-control"
                                            required>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="passPercentage" class="form-label">Passing Percentage<span
                                                class="text-danger ms-1">*</span></label>
                                        <input type="number" name="passPercentage" id="passPercentage"
                                            class="form-control" required min="0" max="100">
                                        <small class="form-text text-muted">Enter a number between 0 and 100 for the
                                            percentage.</small>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Enter
                                            Negative Marking Factor(%) <span class="text-danger ms-1">*</span></label>
                                        <input type="number" name="negativeMarkingFactor" id="negativeMarkingFactor"
                                            class="form-control" placeholder="Enter Negative Marking Factor"
                                            required="required">
                                    </div>
                                    <div class="col-md-6">
                                        <label for="assignedBy" class="form-label">Assigned By<span
                                                class="text-danger ms-1">*</span></label>
                                        <input type="text" name="assignedBy" id="assignedBy" class="form-control"
                                            required value="${assignedBy}">
                                    </div>
                                </div>
                                <div class="col-12 mx-auto mt-4 d-flex justify-content-end">
                                    <button class="py-2 px-3 rounded border bg-blue text-white text-sm me-3"
                                        type="submit">Assign Test</button>
                                    <a href="${pageContext.request.contextPath}/admin/lab/assignTestLab2?id=${tests[0].labTestId}&labTestName=${tests[0].labTestName}"
                                        class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
                <script>

                    document.addEventListener('DOMContentLoaded', function () {
                        const form = document.querySelector('form');
                        const submitButton = form.querySelector('button[type="submit"]');

                        form.addEventListener('submit', function () {
                            submitButton.disabled = true;
                        });
                    });
                    const startDateInput = document.getElementById('startDate');
                    const endDateInput = document.getElementById('endDate');

                    startDateInput.addEventListener('change', function () {
                        endDateInput.min = this.value;
                    });

                    endDateInput.addEventListener('change', function () {
                        startDateInput.max = this.value;
                    });
                    function toggleSidebar() {
                        var sidebar = document.getElementById("sidebar");
                        sidebar.style.display = (sidebar.style.display === "none") ? "block" : "none";
                    }
                    $(document).ready(function () {
                        var labTestCount = ${ tests.size()
                    };
                    if (labTestCount === 1) {
                        var labTestId = "${tests[0].labTestId}";
                        var passingPercentage = "${tests[0].passPercentage}";
                        var labTestName = "${tests[0].labTestName}";
                        $("#labTestId")
                            .val(labTestId)
                            .attr("data-passing-percentage", passingPercentage)
                            .prop("readonly", true);
                        $("#labTestId option:selected").text(labTestName);
                        $("#passPercentage").val(passingPercentage);
                    }
                    var labTestIdElement = document.getElementById("labTestId");
                    var passPercentageElement = document.getElementById("passPercentage");
                    labTestIdElement.addEventListener("change", function () {
                        var selectedOption = labTestIdElement.options[labTestIdElement.selectedIndex];
                        passPercentageElement.value = selectedOption.getAttribute("data-passing-percentage");
                        $("#labTestId option:selected").text(selectedOption.text);
                    });
});
                </script>