<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>
    <div class="row m-0">
      <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
      </div>
      <div class="col-9 m-0 p-2" style="width: 80%">
        <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
          <div class="text-center mt-2">
            <h3>
              Lab Reports
            </h3>
          </div>
          <div class="container m-1">
            <table id="myTable" class="table">
              <thead>
                <tr>
                  <th scope="col">S.No.</th>
                  <th scope="col">Trainee Id</th>
                  <th scope="col">Name</th>
                  <th scope="col">Lab Name</th>
                  <th scope="col">Reports</th>
                </tr>
              </thead>
              <tbody>
                <%! int count=1; %>
                  <c:forEach var="e" items="${labReport}">
                    <tr>
                      <td>
                        <%= count %>
                      </td>
                      <td>${e.traineeId}</td>
                      <td>${e.traineeName}</td>
                      <td>${e.labName}</td>
                      <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm "
                          href="${pageContext.request.contextPath}/admin/viewDetailedLabReports?traineeId=${e.traineeId}&labId=${e.labId}&batchId=${batchId}&labName=${e.labName}">View
                          Detailed Reports</a></td>
                    </tr>
                    <% count++; %>
                  </c:forEach>
                  <% count=1; %>
              </tbody>
            </table>
          </div>


        </div>
      </div>
    </div>