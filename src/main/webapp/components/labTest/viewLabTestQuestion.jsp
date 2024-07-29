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
    <div class="">
      <div class="shadow-sm p-3  bg-white rounded">
        <div class="text-center mt-2">
          <a href="${pageContext.request.contextPath}/admin/addLabTestQuestion?id=${labTestId}&labTestName=${labName}" type="button"
            class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">
            Add New Question
          </a>
          <div style="display: inline-grid;" >
            <h3>
              Lab Test Question Viewer
            </h3>
            <h4 class="bold text-green">${labName}</h4>
          </div>
        </div>
        <div class="container m-1">
          <table id="myTable" class="table">
            <thead>
              <tr>
                <th scope="col">Sr.No</th>
                <th scope="col">Question</th>
                <th scope="col">Query</th>
                <th scope="col">Action</th>
                <th scope="col">Action</th>
              </tr>
            </thead>
            <tbody>
                <c:if test="${not empty questionList}">
                  <c:forEach var="e" items="${questionList}">
                    <tr>
                     <td>1</td>
                      <td>${e.questionDescription}</td>
                      <td>${e.questionAnswer}</td>
                      <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                          href="${pageContext.request.contextPath}/admin/editLabTestQuestion?id=${labTestId}&questionId=${e.questionId}">Edit</a>
                      </td>
                      <td><a class="py-2 px-3 rounded border bg-red text-white text-sm"
                          href="${pageContext.request.contextPath}/admin/deleteLabTestQuestion?id=${labTestId}&questionId=${e.questionId}"
                          onclick="return confirm('Are you sure to delete Question?')">Delete</a></td>
                    </tr>
                  </c:forEach>
                </c:if>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  $(document).ready(function () {
    addSerialNumber();
  });
  function addSerialNumber() {
    $('#myTable tbody tr').each(function (index) {
      var serialNumber = index + 1;
      $(this).find('td:first').text(serialNumber);
    });
  }
</script>