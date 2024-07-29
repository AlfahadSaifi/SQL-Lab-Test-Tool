<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.1.0/dist/sweetalert2.all.min.js"></script>
<div class="text-center mt-5">
    <h3>
        View Records
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
      <th scope="col">Active/Inactive Status</th>
      <th scope="col">Created By</th>
      <th scope="col">Created Date</th>
      <th scope="col">Modified By</th>
      <th scope="col">Modified Date</th>
      <th scope="col">Authorized By</th>
      <th scope="col">Authorized Date</th>
      <th scope="col">Edit</th>
      <th scope="col">View</th>
      <th scope="col">Delete</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach var="e" items="${list}">
    <tr>
      <th scope="row">${e.customerCode}</th>
      <td>${e.customerName}</td>
      <td>${e.contactNumber}</td>
      <td>${e.recordStatus}</td>
      <td>${e.flag}</td>
      <td>${e.createdBy}</td>
      <td>${e.createDate}</td>
      <td>${e.modifiedBy}</td>
      <td>${e.modifiedDate}</td>
      <td>${e.authorizedBy}</td>
      <td>${e.authorizedDate}</td>
      <td><a class="btn btn-primary" href="${pageContext.request.contextPath}/maker/edit/${e.customerCode}" onclick="return confirm('Are you sure want to Edit?')">Edit</a></td>
      <td><button class="btn btn-success" onclick="showData('${e.customerCode}')">View</button></td>
      <td><a class="btn btn-danger"  href="${pageContext.request.contextPath}/maker/delete/${e.customerCode}" onclick="return confirm('Are you sure want to Delete?')">Delete</a></td>
    </tr>
  </c:forEach>
  </tbody>
</table>
</div>
<script type="text/javascript">

        // Function to show and hide the popup
        function togglePopup() {
            $(".content").toggle();
        }
    </script>