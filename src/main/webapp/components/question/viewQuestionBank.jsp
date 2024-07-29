<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp"/>
    </div>
    <div class="col-9 m-0 p-2" style="width: 80%; overflow-y: scroll; height: 540px;">
        <div>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>
        </div>
        <div>
            <div class="shadow-sm p-3 mb-5 bg-white rounded">
                <a href="${pageContext.request.contextPath}/admin/addInQuestionBank" type="button" class="py-2 px-3 rounded border bg-blue text-white text-sm float-end">
                    Add In Question Bank
                </a>
                <div class="text-center mt-5">
                    <h3>Question Bank Viewer</h3>
                </div>
                <div class="container m-1">
                    <table id="myTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th scope="col">S.No.</th>
                                <th scope="col">Question</th>
                                <th scope="col">Query</th>
                                <th scope="col">Title</th> <!-- New column for Title -->
                                <th scope="col">Difficulty</th> <!-- New column for Difficulty -->
                                <th scope="col">Schema Name</th> <!-- New column for Schema Name -->
                                <th scope="col">Action</th>
                                <th scope="col">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="question" items="${questionList}" varStatus="rowStatus">
                                <c:if test="${not empty question}">
                                    <tr>
                                        <td>${rowStatus.count}</td>
                                        <td>${question.questionDescription}</td>
                                        <td>${question.questionAnswer}</td>
                                        <td>${question.topic}</td> <!-- Displaying Title -->
                                        <td>${question.questionDifficulty}</td> <!-- Displaying Difficulty -->
                                        <td>
                                            <c:choose>
                                                <c:when test="${empty question.schema}">
                                                    <!-- If question.schema is null or empty, display a generic value -->
                                                    Generic Question
                                                </c:when>
                                                <c:otherwise>
                                                    ${question.schema.schemaName}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><a class="py-2 px-3 rounded border bg-blue text-white text-sm" href="${pageContext.request.contextPath}/admin/editQuestionInQB?questionId=${question.questionId}">Edit</a></td>
                                        <td><a class="py-2 px-3 rounded border bg-red text-white text-sm" href="${pageContext.request.contextPath}/admin/deleteQuestionInQB?questionId=${question.questionId}" onclick="return confirm('Are you sure to delete Question?')">Delete</a></td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
