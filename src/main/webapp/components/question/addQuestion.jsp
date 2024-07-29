<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
            <%@ page isELIgnored="false" %>
 
<div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
    </div>
    <div class="col-9 m-0 p-2" style="width: 80%">
       
        <div style="display:flex;">
            <div class="container-fluid">
                <div class="row justify-content-center">
                    <div class="col-md-10">
                        <div class="card shadow rounded">
                            <div class="card-body">
                                <h2 class="card-title text-center mb-2">Add Question</h2>
                                <form:form action="addQuestionViaFormInLab?id=${labId}" method="post"
                                            modelAttribute="question" class="needs-validation" id="registration-form">
                                    <div class="mb-2">
                                        <label for="questionDescription" class="form-label">Question Statement</label>
                                        <form:textarea rows="4" cols="50" id="questionDescription" path="questionDescription"
                                                    class="form-control" placeholder="Enter Question"></form:textarea>
                                        <form:errors cssClass="text-danger" path="questionDescription" />
                                    </div>
                                    <div class="mb-2">
                                        <label for="questionAnswer" class="form-label">Actual Query</label>
                                        <form:textarea rows="4" cols="50" id="questionAnswer" path="questionAnswer"
                                                    class="form-control" placeholder="Enter Actual Query"></form:textarea>
                                        <form:errors cssClass="text-danger" path="questionAnswer" />
                                    </div>
                                    <div class="w-50 mb-2">
                                        <label for="questionPoints" class="form-label">Question Point</label>
                                        <form:input id="questionPoints" path="questionPoints" class="form-control"
                                                    placeholder="Enter Question Point" />
                                        <form:errors cssClass="text-danger" path="questionPoints" />
                                    </div>
                                    <div >
                                        <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Save</button>
                                        <button class="py-2 px-3 rounded border bg-green text-white text-sm" type="reset">Reset</button>
                                        <a href="${pageContext.request.contextPath}/admin/viewQuestions?id=${labId}" class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 
            <div>
                <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
                    <form class="row g-3" method="post" action="addQuestionViaExcel?id=${labId}"
                        enctype="multipart/form-data">
                        <div class="mb-3">
                            <label for="formFileSm" class="form-label">Upload Question Excel Sheet</label>
                            <input class="form-control form-control-sm" id="formFileSm" name="file"
                                type="file" required>
                        </div>
                        <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Submit</button>
                    </form>
                </div>
                <!-- upload reference-->
                <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
                    <form class="row g-3" method="post" action="addReferenceFile?id=${labId}"
                        enctype="multipart/form-data">
                        <div class="mb-3">
                            <label for="formFileSm" class="form-label">Upload Lab Reference </label>
                            <input class="form-control form-control-sm" id="formFileSm" name="file"
                                type="file" required>
                                <div class="text-sm text-gray">Upload Pdf Only</div>
                        </div>
                        <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Upload</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
     const queryString = window.location.search;
     const urlParams = new URLSearchParams(queryString);
     const labId = urlParams.get('id')
     $('#loadingSpinner').show();
     $.ajax({
         type: "GET",
         url: "${pageContext.request.contextPath}/api/admin/getQuestionPoint?labId=" + labId,
         contentType: "application/json",
         success: function (data) {
            $("#questionPoints").val(data);
         },
         error: function (xhr, status, error) {
             console.error("Error:", error);
         },
     });
 })
</script>