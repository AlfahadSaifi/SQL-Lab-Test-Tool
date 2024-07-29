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
        <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
          <div class="text-center mt-4 main-heading">
            Lab Test Report
          </div>
          <div class="container m-1">
            <table id="myTable" class="table">
              <thead>
                <tr>
                  <th scope="col">S No.</th>
                  <th scope="col">Lab Test Name</th>
                  <th scope="col">Action </th>
                </tr>
              </thead>
              <tbody>
                <%! int count=1; %>
                  <c:forEach var="e" items="${tests}">
                    <tr>
                      <td>
                        <%= count %>
                      </td>
                      <td>${e.labTestName}</td>
                      <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                          href="${pageContext.request.contextPath}/admin/testReportSummary?id=${e.labTestId}&batchId=${batchId}&batchName=${batchCode}&labTestName=${e.labTestName}">Lab
                          Test Report</a></td>
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