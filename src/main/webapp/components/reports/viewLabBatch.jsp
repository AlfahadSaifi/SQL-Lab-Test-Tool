<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ page isELIgnored="false" %>
    <div class="row m-0">
      <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
      </div>
      <div class="col-9 m-0 p-2" style="width: 80%">
        <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
          <div class="text-center mt-4 main-heading">
            Lab Batches
          </div>
          <div class="container m-1">
            <table id="myTable" class="table">
              <thead>
                <tr>
                  <th scope="col">Batch Code</th>
                  <th scope="col">Action 1</th>
                </tr>
              </thead>
              </thead>
              <tbody>
                <c:forEach var="e" items="${testLabBatches}">
                  <tr>
                    <td>${e.batchCode}</td>
                    <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm"
                        href="${pageContext.request.contextPath}/admin/batchLabs?batchId=${e.id}">Assigned Labs</a></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>