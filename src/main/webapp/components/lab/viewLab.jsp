<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
  <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
    <jsp:include page="/components/sideBar.jsp" />
  </div>
  <div class="col-9 m-0 p-2" style="width: 80%" >
    <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
      <!-- <a href="${pageContext.request.contextPath}/admin/lab" type="button" class="py-2 px-3 rounded border bg-p text-white text-sm float-end">
        Add New Lab
      </a> -->
      <div class="text-center mt-4 main-heading">
          My Labs
      </div>
      <div class="container m-1">
        <table id="myTable" class="table">
          <thead>
            <tr>
              <th scope="col">Lab Name</th>
              <th scope="col">Action 1</th>
              <th scope="col">Action 2</th>
              <th scope="col">Action 3</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="e" items="${labList}">
              <tr>
                <td>${e.labName}</td>
                <td><a class="py-2 px-3 rounded border bg-green text-white text-sm"
                    href="${pageContext.request.contextPath}/admin/viewQuestions?id=${e.labId}">View Questions</a></td>
                <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm "
                    href="${pageContext.request.contextPath}/admin/lab/assignLab1?id=${e.labId}">Assign Practice</a>
                </td>
                <td><a class="py-2 px-3 rounded border bg-red text-white text-sm"
                    href="${pageContext.request.contextPath}/admin/testReportSummary?id=${e.labId}">View Reports</a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>