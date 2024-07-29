<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ page isELIgnored="false" %>
        <div class="row m-0">
            <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                <jsp:include page="/components/sideBar.jsp" />
            </div>
            <div class="col-9 m-0 p-2" style="width: 80%;">
                <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
                    <div class="d-flex justify-content-center align-items-center">
                        <div class="w-50 border border-gray shadow p-3 bg-white rounded mt-5"
                            style="margin-bottom: 90px">
                            <h2 class="text-center mt-3">Assign Lab</h2>
                            <form action="assignLab2" method="post">
                                <div class="row mt-4">
                                    <div class="col-md-6">
                                        <label class="form-label text-md text-dark mb-1">Select a Lab</label>
                                        <select name="labId" id="labId" class="form-select form-select-sm" required>
                                            <option disabled selected>-- Select a Lab ---</option>
                                            <c:forEach var="e" items="${labs}">
                                                <option value="${e.labId}">${e.labName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label text-md text-dark mb-1">Select a Batch</label>
                                        <select name="batch" class="form-select form-select-sm" required>
                                            <option disabled selected>-- Select a Batch ---</option>
                                            <c:forEach var="e" items="${batches}">
                                                <option value="${e.id}">${e.batchCode}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="text-center mt-4">
                                    <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Assign Lab</button>
                                    <a href="${pageContext.request.contextPath}/admin/lab/assignLab1?id=${labs[0].labId}"
                                        class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
        <script>
            $(document).ready(function () {
                const urlParams = new URLSearchParams(window.location.search);
                const labId = urlParams.get('id');
            //     var labCount = ${labs.size()};
            // if (labCount === 1) {
            //     var labId = "${labs[0].labId}";
            //     // Set the selected option and make it readonly
            //     $("#labId")
            //         .val(labId)
            //         .prop("readonly", true);
            // }
            });
        </script>