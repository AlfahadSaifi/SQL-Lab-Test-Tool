<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<div class="text-center mt-5">
    <h3>
        Pending Request Records
    </h3>
</div>

<div class="container m-1">
<table id="myTable" class="table">
  <thead>
    <tr>
      <th scope="col">Customer Code</th>
      <th scope="col">Name</th>
      <th scope="col">Contact Number</th>
      <th scope="col">Record Status</th>
      <th scope="col">Requested By</th>
      <th scope="col">Requested Date</th>
      <th scope="col">Approved</th>
      <th scope="col">Reject</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="e" items="${list}">
    <tr>
      <th scope="row">${e.customerCode}</th>
      <td>${e.customerName}</td>
      <td>${e.contactNumber}</td>
      <td>${e.recordStatus}</td>
      <td>${e.createdBy}</td>
      <td>${e.createDate}</td>
      <td><a class="btn btn-primary" href="${pageContext.request.contextPath}/checker/approve/${e.customerCode}" onclick="return confirm('Are you sure want to Approve?')">Approve</a></td>
      <td><a class="btn btn-primary" href="${pageContext.request.contextPath}/checker/reject/${e.customerCode}" onclick="return confirm('Are you sure want to Reject?')">Reject</a></td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</div>