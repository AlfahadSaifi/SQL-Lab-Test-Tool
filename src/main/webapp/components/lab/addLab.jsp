<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
    <%@ page isELIgnored="false" %>
      <div class="row m-0">
        <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
          <jsp:include page="/components/sideBar.jsp" />
        </div>
        <div class="col-9 m-0 p-2" style="width: 80%">
          <div class="container-fluid shadow p-5 mb-5 bg-white rounded">
            <div style="display:flex;">
              <div class="container-fluid my-3">
                <div class="w-50 border border-gray shadow p-3 bg-white rounded mx-auto">
                  <div class="text-center my-2 main-heading">Create Lab</div>
                  <form:form action="lab" method="POST" modelAttribute="lab" class="needs-validation"
                    id="registration-form">
                    <div class="row mb-3">
                      <div class="col-md-6" style="width: 100%">
                        <form:label class="form-label text-md text-dark mb-1" path="labName">Enter Lab Name</form:label>
                        <form:input path="labName" class="form-control" placeholder="Enter Lab Name"
                          required="required" />
                        <form:errors cssStyle="color:blue" path="labName" />
                        <br>
                        <form:label class="form-label text-md text-dark mb-1" path="pointsPerQuestion">Enter Point Per
                          Question</form:label>
                        <select class="form-select form-select-sm" name="pointsPerQuestion" required="required">
                          <option value="1">1</option>
                          <option value="2">2</option>
                          <option value="3">3</option>
                          <option value="4">4</option>
                          <option value="5">5</option>
                        </select>
                        <form:errors cssStyle="color:blue" path="pointsPerQuestion" />
                        <br>
                        <form:label class="form-label text-md text-dark mb-1" path="negativeMarkingFactor">Enter
                          Negative Marking Factor(%)</form:label>
                        <form:input path="negativeMarkingFactor" class="form-control"
                          placeholder="Enter Negative Marking Factor" required="required" />
                        <form:errors cssStyle="color:blue" path="negativeMarkingFactor" />
                      </div>
                    </div>
                    <div class="d-flex justify-content-end gap-2 ">
                      <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Create Lab</button>
                      <a href="${pageContext.request.contextPath}/admin/viewAssignLab" class="btn btn-danger bg-red">Cancel</a>
                    </div>
                  </form:form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>