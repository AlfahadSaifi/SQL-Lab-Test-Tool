<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib
prefix="form" uri="http://www.springframework.org/tags/form" %> <%@ taglib
prefix="spring" uri="http://www.springframework.org/tags" %> <%@ taglib
prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%@ page
isELIgnored="false" %>
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
    <div class="">
      <div class="shadow-sm p-3 bg-white rounded">
        <div class="text-center my-2 mb-4">
          <a
            href="${pageContext.request.contextPath}/admin/registerTraineeMore?id=${batchId}"
            type="button"
            class="py-2 px-3 rounded border bg-blue text-white text-sm float-end"
          >
            Add Trainee
          </a>
          <h3>View Trainee</h3>
          <h4 class="bold text-green">${labName}</h4>
        </div>
        <div class="container m-1">
          <table id="myTable" class="table">
            <thead>
              <tr>
                <th scope="col">TraineeId</th>
                <th scope="col">Name</th>
                <th scope="col">Email</th>
                <th scope="col">Action</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="e" items="${trainees}">
                <tr>
                  <td>${e.employeeId}</td>
                  <td>${e.name}</td>
                  <td>${e.emailId}</td>
                  <td>
                    <a
                      class="py-2 px-3 rounded border bg-blue text-white text-sm"
                      href="${pageContext.request.contextPath}/admin/editTraineeDetail?id=${e.employeeId}&batchId=${batchId}"
                      >Edit/View</a
                    >
                  </td>
                  <td>
                    <a
                      class="py-2 px-3 rounded border bg-green text-white text-sm"
                      href="${pageContext.request.contextPath}/admin/traineeDetailReport?traineeId=${e.employeeId}&batchId=${batchId}"
                      >Report</a
                    >
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
