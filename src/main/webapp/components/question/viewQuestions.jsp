<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<div class="row m-0">
<div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
<jsp:include page="/components/sideBar.jsp"/>
</div>
<div class="col-9 m-0 p-2"  style="width: 80%; overflow-y: scroll; height: 540px;"  >
  <div>
    <c:if test="${not empty successMessage}">
      <div class="alert alert-success">${successMessage}</div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
  </div>
  <div class="">
    <div class="shadow-sm p-3 mb-5 bg-white rounded">
      <a href="${pageContext.request.contextPath}/admin/addQuestion?id=${labId}" type="button" class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">
        Add New Question
      </a>
      <div class="text-center mt-5">
        <h3>
          Lab Question Viewer
        </h3>        
      </div>
<div class="container m-1">
  <table id="myTable" class="table">
    <thead>
      <tr>
        <th scope="col">S.No.</th>
      <th scope="col">Question</th>
      <th scope="col">Query</th>
      <th scope="col">Action</th>
      <th scope="col">Action</th>
    </tr>
  </thead>
  <tbody>
    <%! int count = 1; %>
    <c:if test="${not empty questionList}">
      <c:forEach var="e" items="${questionList}">
        <tr>
          <td><%= count %></td>
          <td>${e.questionDescription}</td>
      <td>${e.questionAnswer}</td>
      <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm" href="${pageContext.request.contextPath}/admin/editQuestion?id=${labId}&questionId=${e.questionId}">Edit</a></td>
      <td><a class="py-2 px-3 rounded border bg-red text-white text-sm" href="${pageContext.request.contextPath}/admin/deleteQuestion?id=${labId}&questionId=${e.questionId}" onclick="return confirm('Are you sure to delete Question?')">Delete</a></td>
    </tr>
    <% count++; %>
  </c:forEach>
  <% count=1; %>
</c:if>

</tbody>
</table>
</div>
</div>


                </div>
            </div>
</div>
</div>
