<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="row m-0">
    <div class="col-3 m-0 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
    </div>
    <div class="col-9 m-0 p-0">
        <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
            <div style="display:flex; margin-top:20px;">
                <div class="container-fluid m-8 w-100 shadow-sm p-3 mb-5 bg-white rounded">
                    <h3 class="display-2 text-center p-3 mb-4 bg-primary text-white rounded border" style="border-width: 2px;">Deassign Test</h3>
                    <form action="deassignTest" method="post">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label">Select a Test</label>
                                <select name="labId" class="form-select form-select-sm" required>
                                    <option>-- Select a Test ---</option>
                                    <c:forEach var="e" items="${tests}">
                                        <option value="${e.labTestId}">${e.labTestName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Select a Batch</label>
                                <select name="batch" class="form-select form-select-sm" required>
                                    <option>-- Select a Batch ---</option>
                                    <c:forEach var="e" items="${batches}">
                                        <option value="${e.id}">${e.batchCode}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div><br/>
                          <div class="row g-3">

                                                    <div class="col-md-6">
                                                        <label class="form-label">DeAssigned By</label>
                                                        <input type="text" name="deassignedBy" class="form-control" required>
                                                    </div>

            <div class="col-md-6">
                                                                    <label class="form-label">Reasons* </label>
                                                                    <input type="text" name="Reason" class="form-control" required>
                                                                </div>
                                                </div>
                        <br>
                        <div class="d-grid gap-2 col-6 mx-auto">
                            <button class="btn btn-primary" type="submit">Deassign Test</button>
                        </div>
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/admin/" class="btn btn-danger bg-red">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
