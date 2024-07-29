<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<form action="addInQuestionBankViaForm" method="post" class="needs-validation" id="registration-form" enctype="multipart/form-data">
<div class="mb-2">
<label for="questionDescription" class="form-label">Question Statement</label>
<textarea rows="4" cols="50" id="questionDescription" name="questionDescription" class="form-control" placeholder="Enter Question"></textarea>
</div>
<div class="mb-2">
<label for="questionAnswer" class="form-label">Actual Query</label>
<textarea rows="4" cols="50" id="questionAnswer" name="questionAnswer" class="form-control" placeholder="Enter Actual Query"></textarea>
</div>
<div class="w-50 mb-2">
<label for="questionPoints" class="form-label">Question Point</label>
<input id="questionPoints" name="questionPoints" class="form-control" placeholder="Enter Question Point" />
</div>
<div class="w-50 mb-2">
<label for="topic" class="form-label">Enter Topic</label>
<input id="topic" name="topic" class="form-control" placeholder="Enter Topic" />
</div>
<div class="mb-2">
<label for="difficulty" class="form-label">Select Difficulty</label>
<select class="form-select" id="difficulty" name="difficulty">
<option value="EASY">Easy</option>
<option value="MEDIUM">Medium</option>
<option value="HARD">Hard</option>
</select>
</div>
<div class="mb-2">
<label class="form-label">Select Schema Type</label><br>
<div class="form-check form-check-inline">
<input class="form-check-input" type="radio" name="schemaType" id="existingSchema" value="existing">
<label class="form-check-label" for="existingSchema">Existing Schema</label>
</div>
<div class="form-check form-check-inline">
<input class="form-check-input" type="radio" name="schemaType" id="newSchema" value="new">
<label class="form-check-label" for="newSchema">New Schema</label>
</div>
<div class="form-check form-check-inline">
<input class="form-check-input" type="radio" name="schemaType" id="genericSchema" value="generic">
<label class="form-check-label" for="genericSchema">Generic Schema</label>
</div>
</div>
<div id="existingSchemaFields" style="display:none;">
<div class="mb-2">
<label for="existingSchemaName" class="form-label">Select Existing Schema</label>
<select class="form-select" id="existingSchemaName" name="existingSchemaId">
<c:forEach items="${schemaList}" var="schema">
<option value=${schema.schemaReferenceId}>${schema.schemaName}</option>
</c:forEach>
</select>
</div>
</div>
<div id="newSchemaFields" style="display:none;">
<div class="mb-2">
<label for="newSchemaName" class="form-label">Enter New Schema Name</label>
<input type="text" class="form-control" id="newSchemaName" name="newSchemaName" />
</div>
<div class="mb-2">
<label for="referenceFile" class="form-label">Upload Lab Reference (PDF)</label>
<input class="form-control" type="file" id="referenceFile" name="referenceFile" accept=".pdf"/>
</div>

</div>
<div>
<button class="py-2 px-3 rounded border bg-blue text-white text-sm" type="submit">Save</button>
<button class="py-2 px-3 rounded border bg-green text-white text-sm" type="reset">Reset</button>
<a href="${pageContext.request.contextPath}/admin/viewQuestionBank" class="py-2 px-3 rounded border bg-red text-white text-sm">Cancel</a>
</div>
</form>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
</div>
<script>
    $(document).ready(function () {
        $('input[type=radio][name=schemaType]').change(function() {
            if (this.value === 'existing') {
                $('#existingSchemaFields').show();
                $('#newSchemaFields').hide();
                $('#existingSchemaName').attr('required', true);
                $('#newSchemaName').removeAttr('required');
                $('#referenceFile').removeAttr('required');
            } else if (this.value === 'new') {
                $('#existingSchemaFields').hide();
                $('#newSchemaFields').show();
                $('#existingSchemaName').removeAttr('required');
                $('#newSchemaName').attr('required', true);
                $('#referenceFile').attr('required', true);
            } else if (this.value === 'generic') {
                $('#existingSchemaFields').hide();
                $('#newSchemaFields').hide();
                $('#existingSchemaName').removeAttr('required');
                $('#newSchemaName').removeAttr('required');
                $('#referenceFile').removeAttr('required');
            }
        });
    });
</script>
