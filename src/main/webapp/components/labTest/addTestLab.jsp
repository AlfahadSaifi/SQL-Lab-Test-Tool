<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
        <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
            <%@ page isELIgnored="false" %>

                <div class="row m-0">
                    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                        <jsp:include page="/components/sideBar.jsp" />
                    </div>
                    <div class="col-9 m-0 p-2" style="width: 80%">
                        <div class="container-fluid shadow p-3 py-5 mb-1 bg-white rounded">
                          
                                <div class="container-fluid w-50 shadow p-3 mb-1 bg-white rounded">
                                    <h2 class="text-center pt-2">Create Lab Test</h2>
                                    <form:form action="createLabTest" method="post" modelAttribute="labTest"
                                        class="needs-validation" id="registration-form">
                                        <div class="row p-2">
                                            <div class="col-md-12 my-2">
                                                <form:label class="text-md text-dark-100 mb-1" path="labTestName">Lab
                                                    Test Name
                                                </form:label>
                                                <form:input type="text" path="labTestName" class="form-control"
                                                    placeholder="Enter Lab Test Name" required="required" />
                                                <form:errors cssStyle="color:blue" path="labTestName" /><br />
                                                <form:label class="form-label text-md text-dark-100 mb-1"
                                                    path="pointsPerQuestion">Enter Point Per
                                                    Question</form:label>
                                                <select class="form-select form-select-sm" name="pointsPerQuestion"
                                                    required="required">
                                                    <option value="1">1</option>
                                                    <option value="2">2</option>
                                                    <option value="3">3</option>
                                                    <option value="4">4</option>
                                                    <option value="5">5</option>
                                                </select>
                                                <form:errors cssStyle="color:blue" path="pointsPerQuestion" />
                                                <br />
                                                <form:label class="text-md text-dark-100 mb-1" path="passPercentage">
                                                    Passing Percentage
                                                </form:label>
                                                <form:input type="text" path="passPercentage" class="form-control"
                                                    placeholder="Enter Percentage" required="required" />
                                                <form:errors cssStyle="color:blue" path="passPercentage" />
                                                <br/>
                                                <div class="d-flex justify-content-end gap-2">
                                                    <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Create</button>
                                                    <a href="${pageContext.request.contextPath}/admin/viewAssignLabTest"
                                                    class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                                                </div>
                                            </div>
                                        </div>
                                    </form:form>
                                </div>
                          
                        </div>
                    </div>
                </div>