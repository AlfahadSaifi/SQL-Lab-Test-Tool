<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
            <%@ page isELIgnored="false" %>

            <div class="row m-0">
                <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
                    <jsp:include page="/components/sideBar.jsp" />
                </div>
                <div class="col-9 m-0 p-0" style="width: 60%">
                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success">${successMessage}</div>
                      </c:if>
                      <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger">${errorMessage}</div>
                      </c:if>
                    <div class="container-fluid w-75 shadow-sm p-3 mb-5 bg-white rounded">
                        <h2 class="text-center mt-2 mb-4">Edit Question</h2>
                        <form:form action="editQuestionInQBViaForm?questionId=${question.questionId}"
                            method="post" modelAttribute="question" class="needs-validation" id="registration-form">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <form:label class="form-label" path="questionDescription">Question Statement</form:label>
                                        <form:textarea rows="4" cols="65" path="questionDescription" class="form-control"
                                            placeholder="Enter Question"></form:textarea>
                                        <form:errors cssClass="text-danger" path="questionDescription" />
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <form:label class="form-label" path="questionAnswer">Actual Query</form:label>
                                        <form:textarea rows="4" cols="65" path="questionAnswer" class="form-control"
                                            placeholder="Enter Actual Query"></form:textarea>
                                        <form:errors cssClass="text-danger" path="questionAnswer" />
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <form:label class="form-label" path="questionPoints">Question Points</form:label>
                                        <form:input path="questionPoints" placeholder="Enter Points" class="form-control" />
                                        <form:errors cssClass="text-danger" path="questionPoints" />
                                    </div>
                                </div>
                                <div class="col-md-12 mt-4">
                                    <div class="form-group">
                                        <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Save</button>
                                        <a href="${pageContext.request.contextPath}/admin/viewQuestionBank"
                                            class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                                    </div>
                                </div>
                            </div>
                        </form:form>
                    </div>
                    
                </div>


            </div>