<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
  <div class="col-3 m-0 p-0" style="width: 20%">
    <jsp:include page="/components/sideBar.jsp" />
  </div>
  <div class="col-9 m-0 p-0">
    <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
      <div class="text-center mt-4 main-heading">
        My Labs Report
      </div>
      <div class="container m-1">
        <table id="myTable" class="table">
          <thead>
            <tr>
              <th scope="col">S No.</th>
              <th scope="col">Lab Name</th>
              <th scope="col">Reports</th>
            </tr>
          </thead>
              <tbody>
                <%! int count = 1; %>
                <c:forEach var="e" items="${labs}">
                  <tr>
                    <td><%= count %></td>
                    <td>${e.labName}</td>
                    <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                        href="${pageContext.request.contextPath}/admin/viewReports?id=${e.labId}">View Reports</a></td>
                  </tr>
                  <% count++; %>
                </c:forEach>
                <% count = 1; %>
              </tbody>
            </table>
          </div>


        </div>
      </div>
    </div>