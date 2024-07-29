<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
  <div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
      <jsp:include page="/components/sideBar.jsp" />
    </div>
    <div class="col-9 m-0 p-2" style="width: 80%" >
      <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
        <div class="text-center mt-5">
          <h3>
            Lab Reports
          </h3>
        </div>
        <div class="container m-1">
          <table id="myTable" class="table">
            <thead>
              <tr>
                <th scope="col">Id</th>
                <th scope="col">Trainee Id</th>
                <th scope="col">Name</th>
                <th scope="col">Reports</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="e" items="${traineeLabReportsDtos}">
                <tr>
                  <td>${e.id}</td>
                  <td>${e.trainee.employeeId}</td>
                  <td>${e.trainee.name}</td>
                  <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                      href="${pageContext.request.contextPath}/admin/viewDetailedReports?id=${e.id}&id1=${labId}">View
                      Detailed Reports</a></td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>