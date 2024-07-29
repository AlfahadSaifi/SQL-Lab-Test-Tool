<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
<div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
    </div>
<div class="col-9 m-0 p-0" style="width: 80%" >
    <div style="display: flex;">
        <div class="container-fluid m-8 w-75 shadow-sm p-3 mb-1 bg-white rounded">
            <h2 class="text-center mt-8 pt-1">Edit Lab Test Question</h2>
            <form:form action="editLabTestQuestionViaForm?id=${labId}&questionId=${question.questionId}"
                method="post" modelAttribute="question" class="needs-validation" id="registration-form">
                <div class="row">
                    <div class="col-md-12 mb-3">
                        <form:label class="form-label" path="questionDescription">Question Statement</form:label>
                        <form:textarea class="form-control" rows="4" path="questionDescription"
                            placeholder="Enter Question" />
                        <form:errors cssClass="text-danger" path="questionDescription" />
                    </div>
                    <div class="col-md-12 mb-3">
                        <form:label class="form-label" path="questionAnswer">Actual Query</form:label>
                        <form:textarea class="form-control" rows="4" path="questionAnswer"
                            placeholder="Enter Actual Query" />
                        <form:errors cssClass="text-danger" path="questionAnswer" />
                    </div>
                    <div class="col-md-6 mb-3">
                        <form:label class="form-label" path="questionPoints">Question Points</form:label>
                        <form:input class="form-control" path="questionPoints"
                            placeholder="Enter Question Points" />
                        <form:errors cssClass="text-danger" path="questionPoints" />
                    </div>
                </div>
                <div class="d-flex gap-2">
                    <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Save</button>
                    <a href="${pageContext.request.contextPath}/admin/viewLabTestQuestions?id=${labId}" class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                </div>
            </form:form>
        </div>
    </div>
</div>

</div>
