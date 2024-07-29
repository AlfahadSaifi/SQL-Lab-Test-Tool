<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ page isELIgnored="false" %>
    <div class="row m-0">
      <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
      </div>
      <div class="col-9 m-0 p-2" style="width: 80%">
        <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
          <div class="text-center mt-4 main-heading">
            Trainees Report
          </div>
          <div class="container">
            <table id="myTable" class="table">
              <thead>
                <tr>
                <th scope="col">Serial No.</th>
                  <th scope="col">TraineeId</th>
                  <th scope="col">Trainee Name</th>
                  <th scope="col">Batch Name</th>
                  <th scope="col">Action</th>
                </tr>
              </thead>
              </thead>
              <tbody>
               <%! int count = 1; %>
                <c:forEach var="e" items="${TraineeList}">
                  <tr>
                   <td><%= count %></td>
                    <td>${e.traineeId}</td>
                    <td>${e.traineeName}</td>
                    <td>${e.batchCode}</td>
                    <td><a class="py-2 px-3 rounded border bg-green text-white text-sm"
                        href="${pageContext.request.contextPath}/admin/traineeDetailReport?traineeId=${e.traineeId}&batchId=${e.batchId}">View Reports</a></td>
                  <% count++; %>
                  </tr>
                </c:forEach>
                <% count=1; %>
              </tbody>
            </table>
          </div>
        </div>

      </div>
    </div>