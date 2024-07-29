<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>

<div class="row m-0">
    <div id="sidebar" class="col-3 mt-4 p-0" style="width: 20%">
        <jsp:include page="/components/sideBar.jsp" />
    </div>
    <div class="col-9 m-0 p-2 mb-4" style="width: 80%">
        <div style="display:flex;">
            <div class="container-fluid">
                <div class="shadow p-4 bg-white rounded w-100">
                    <h4 class="text-center mb-2">Lab Test Name: <span
                            class="text-green">${labTestName}</span></h4>
                    <h3 class="text-center mb-2">Add Question</h3>
                    <form:form action="addLabTestQuestionViaForm?id=${labTestId}" method="post"
                        modelAttribute="question" class="needs-validation" id="registration-form">
                        <div class="row mb-3">
                            <div class="col-md-12">
                                <label class="form-label" for="questionDescription">Question
                                    Statement</label>
                                <textarea class="form-control" rows="4" cols="50" id="questionDescription"
                                    name="questionDescription" placeholder="Enter Question"
                                    maxlength="400" required></textarea>
                                <small class="m-1"><span id="charCount">0</span>/400 characters</small>
                                <div class="text-danger" id="charExceedMsg" style="display: none;">Character
                                    limit exceeded!</div>
                            </div>

                        </div>
                        <div class="row mb-3">
                            <div class="col-md-12">
                                <label class="form-label" for="questionAnswer">Actual Query</label>
                                <textarea class="form-control" rows="4" cols="50" id="questionAnswer"
                                    name="questionAnswer" required placeholder="Enter Actual Query"
                                    maxlength="500"></textarea>
                                <small class="m-1"><span id="answerCharCount">0</span>/400 characters</small>
                                <div class="text-danger" id="answerCharExceedMsg" style="display: none;">
                                    Character limit exceeded!</div>
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label" for="questionPoints">Question Point</label>
                                <input type="number" class="form-control" id="questionPoints"
                                    name="questionPoints" placeholder="Enter Question Point" min="1" />
                                <form:errors cssClass="text-danger" path="questionPoints" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <button class="py-2 px-3 rounded border bg-blue text-white text-sm"
                                    type="submit">Save</button>
                                <button class="py-2 px-3 rounded border bg-green text-white text-sm"
                                    type="reset">Reset</button>
                                <a href="${pageContext.request.contextPath}/admin/viewLabTestQuestions?id=${labTestId}"
                                    class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="w-50">
                <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
                    <form class="row g-3" method="post" action="addLabTestQuestionViaExcel?labTestId=${labTestId}"
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
                    <form class="row g-3" method="post" action="addLabTestReferenceFile?id=${labTestId}"
                        enctype="multipart/form-data">
                        <div class="mb-3">
                            <label for="formFileSm" class="form-label">Upload Lab Reference</label>
                            <input class="form-control form-control-sm" id="formFileSm" name="file"
                                type="file" required>
                                <div class="text-sm text-gray">Upload Pdf Only</div>
                        </div>
                        <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Upload</button>
                    </form>
                </div>

                 <div class="container-fluid shadow p-3 mb-5 bg-white rounded">
                       <form class="row g-3" method="post" action="${pageContext.request.contextPath}/admin/lab/addFromQuestionBank?id=${labTestId}"
                            enctype="multipart/form-data">
                            <div class="mb-3">
                            <button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Add from Question Bank</button>
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
        const labTestId = urlParams.get('id')
        $('#loadingSpinner').show();
        $.ajax({
            type: "GET",
            url: "${pageContext.request.contextPath}/api/admin/getLabTestQuestionPoint?labTestId=" + labTestId,
            contentType: "application/json",
            success: function (data) {
                $("#questionPoints").val(data);
            },
            error: function (xhr, status, error) {
                console.error("Error:", error);
            },
        });

        $('#questionDescription').on('input', function () {
            var text = $(this).val();
            var remainingChars = 400 - text.length;

            $('#charCount').text(text.length);

            if (remainingChars < 0) {
                $('#charCount').css('color', 'red');
                $('#charExceedMsg').show();
            } else {
                $('#charCount').css('color', '');
                $('#charExceedMsg').hide();
            }
        });
        $('#questionAnswer').on('input', function () {
            var text = $(this).val();
            var remainingChars = 400 - text.length;

            $('#answerCharCount').text(text.length);

            if (remainingChars < 0) {
                $('#answerCharCount').css('color', 'red');
                $('#answerCharExceedMsg').show();
            } else {
                $('#answerCharCount').css('color', '');
                $('#answerCharExceedMsg').hide();
            }
        });
    })

</script>