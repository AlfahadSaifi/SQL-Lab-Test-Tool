<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ page isELIgnored="false" %>

            <div class="row m-0">
                <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                    <jsp:include page="/components/sideBar.jsp" />
                </div>

                <div class="col-9 m-0 p-2" style="width: 80%">
                    <div class="">
                        <div class="container-fluid shadow p-3 mb-2 bg-white rounded">
                            <div style="display:flex; margin-top:30px;">
                                <div class="container-fluid m-8 w-50 shadow p-3 mb-2 bg-white rounded">
                                    <h3 class="text-center mt-8 p-2">Create New Batch</h3>
                                    <form:form action="batch" method="post" modelAttribute="batch"
                                        class="needs-validation p-2" id="registration-form">
                                        <div class="row">
                                            <div class="col-md-12">
                                                <form:label class="text-md text-dark-100 mb-1" path="batchCode">Enter
                                                    Batch Code
                                                </form:label>
                                                <form:input path="batchCode" class="form-control"
                                                    placeholder="Enter Batch Code" required="required" />
                                                <form:errors cssStyle="color:blue" path="batchCode" /><br />

                                                <form:label class="text-md text-dark-100 mb-1" path="enrollmentDate">
                                                    Enter Batch Date</form:label>
                                                <form:input path="enrollmentDate" id="enrollmentDate"
                                                    class="form-control" type="date" required="required" />
                                                <form:errors cssStyle="color:blue" path="enrollmentDate" /><br />

                                                <form:label class="text-md text-dark-100 mb-1"
                                                    path="enrollmentExpiryDate">Enter Batch Closing Date
                                                </form:label>
                                                <form:input path="enrollmentExpiryDate" id="enrollmentExpiryDate"
                                                    class="form-control" type="date" required="required" />
                                                <form:errors cssStyle="color:blue" path="enrollmentExpiryDate" />
                                            </div>
                                        </div>
                                        <br />
                                        <div class="d-flex justify-content-end gap-2">
                                            <button class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                                type="submit">
                                                Create
                                            </button>
                                            <a href="${pageContext.request.contextPath}/admin/viewBatches"
                                                class="py-2 px-3 rounded border bg-red text-white text-sm">
                                                Cancel
                                            </a>
                                        </div>
                                    </form:form>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script>
                const startDateInput = document.getElementById('enrollmentDate');
                const endDateInput = document.getElementById('enrollmentExpiryDate');

                startDateInput.addEventListener('change', function () {
                    endDateInput.min = this.value;
                });

                endDateInput.addEventListener('change', function () {
                    startDateInput.max = this.value;
                });
            </script>