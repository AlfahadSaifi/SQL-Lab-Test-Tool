<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>

<div class="row m-0">
  <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
    <jsp:include page="/components/sideBar.jsp" />
  </div>
  <div class="col-9 m-0 p-2" style="width: 80%">
    <div class="container-fluid shadow-sm p-3 mb-5 bg-white rounded">
      <!-- <a href="${pageContext.request.contextPath}/admin/createLabTest" type="button"
        class="py-2 px-3 rounded border bg-p text-white text-sm float-end">
        Add New Lab Test
      </a> -->
      <div class="text-center mt-5">
        <h3>
          Lab Test Viewer
        </h3>

      </div>
      <div class="container m-1">
        <table id="myTable" class="table">
          <thead>
            <tr>
              <th scope="col">Lab Test Name</th>
              <th scope="col">Action 1</th>
              <th scope="col">Action 2</th>
              <th scope="col">Action 3</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="e" items="${labTestList}">
              <tr>
                <td>${e.labTestName}</td>
                <td><a class="py-2 px-3 rounded border bg-green text-white text-sm"
                  href="${pageContext.request.contextPath}/admin/viewLabTestQuestions?id=${e.labTestId}">View
                  Questions</a></td>
                <!-- <td><a class="btn btn-primary"
                    href="${pageContext.request.contextPath}/admin/addLabTestQuestion?id=${e.labTestId}&labName=${e.labTestName}">Add
                    Questions</a></td> -->
                <!-- <td><a class="btn btn-success"
                    href="${pageContext.request.contextPath}/admin/viewLabTestQuestions?id=${e.labTestId}">View
                    Questions</a></td> -->
                
                    <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                      href="${pageContext.request.contextPath}/admin/lab/assignTestLab1?id=${e.labTestId}">Assign
                      Lab Test</a></td> 
                    <td><a class="py-2 px-3 rounded border bg-red text-white text-sm"
                      href="${pageContext.request.contextPath}/admin/testReportSummary?id=${e.labTestId}&batchId=${batchId}&batchName=${batchCode}&labTestName=${e.labTestName}">View
                      Report</a></td> 
                      <!-- <td><a class="py-2 px-3 rounded border bg-red text-white text-sm"
                        href="${pageContext.request.contextPath}/admin/deleteLabTest?id=${e.labTestId}"
                        onclick="return confirm('Are you sure to delete lab test?')">Delete</a></td> -->
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</div>

